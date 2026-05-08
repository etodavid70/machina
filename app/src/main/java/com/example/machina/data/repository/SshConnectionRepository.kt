package com.example.machina.data.repository

import com.example.machina.data.model.dashboard_models.SshConnectionRequest
import com.example.machina.data.model.dashboard_models.SshConnectionResult
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import java.io.ByteArrayOutputStream
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Properties

class SshConnectionRepository {

    fun connect(request: SshConnectionRequest): SshConnectionResult {
        val jsch = JSch()

        request.privateKey?.let { privateKey ->
            jsch.addIdentity(
                request.privateKeyName ?: "machina-uploaded-key",
                privateKey,
                null,
                request.password?.takeIf { it.isNotBlank() }?.toByteArray()
            )
        }

        val session = jsch.getSession(request.username.trim(), request.host.trim(), request.port)
        request.password?.takeIf { it.isNotBlank() }?.let(session::setPassword)

        val config = Properties().apply {
            put("StrictHostKeyChecking", "no")
            put("PreferredAuthentications", preferredAuthentications(request))
        }

        session.setConfig(config)
        session.timeout = CONNECT_TIMEOUT_MS

        try {
            session.connect(CONNECT_TIMEOUT_MS)

            val output = runValidationCommand(session)

            return SshConnectionResult(
                host = request.host.trim(),
                username = request.username.trim(),
                port = request.port,
                output = output.ifBlank { "SSH connection established." }
            )
        } catch (e: JSchException) {
            throw IllegalStateException(buildFailureMessage(request, e), e)
        } finally {
            if (session.isConnected) {
                session.disconnect()
            }
        }
    }

    private fun preferredAuthentications(request: SshConnectionRequest): String {
        return if (request.privateKey != null) {
            "publickey,password,keyboard-interactive"
        } else {
            "password,keyboard-interactive"
        }
    }

    private fun runValidationCommand(session: com.jcraft.jsch.Session): String {
        val channel = session.openChannel("exec") as ChannelExec
        val output = ByteArrayOutputStream()
        val error = ByteArrayOutputStream()

        channel.setCommand("printf 'connected:%s@%s' \"$(whoami)\" \"$(hostname)\"")
        channel.inputStream = null
        channel.outputStream = output
        channel.setErrStream(error)

        try {
            channel.connect(COMMAND_TIMEOUT_MS)

            val deadline = System.currentTimeMillis() + COMMAND_TIMEOUT_MS
            while (!channel.isClosed && System.currentTimeMillis() < deadline) {
                Thread.sleep(100)
            }

            if (!channel.isClosed) {
                throw IllegalStateException("SSH command timed out")
            }

            val errorMessage = error.toString(Charsets.UTF_8.name()).trim()
            if (channel.exitStatus != 0 && errorMessage.isNotBlank()) {
                throw IllegalStateException(errorMessage)
            }

            return output.toString(Charsets.UTF_8.name()).trim()
        } finally {
            if (channel.isConnected) {
                channel.disconnect()
            }
        }
    }

    private fun buildFailureMessage(request: SshConnectionRequest, error: JSchException): String {
        val cause = error.cause
        return when {
            cause is SocketTimeoutException || error.message?.contains("timeout", ignoreCase = true) == true -> {
                "Could not reach ${request.host}:${request.port}. Check that the server is online, SSH is running, port ${request.port} is open, and you are using a reachable public IP or the same VPN/network."
            }
            cause is UnknownHostException -> {
                "Could not resolve ${request.host}. Check the host/IP address."
            }
            error.message?.contains("Auth fail", ignoreCase = true) == true -> {
                "SSH authentication failed. Check the username, password, PEM key, or key passphrase."
            }
            error.message?.contains("Connection refused", ignoreCase = true) == true -> {
                "Connection refused by ${request.host}:${request.port}. Check that SSH is running and listening on port ${request.port}."
            }
            else -> error.message ?: "SSH connection failed"
        }
    }

    private companion object {
        const val CONNECT_TIMEOUT_MS = 15_000
        const val COMMAND_TIMEOUT_MS = 10_000
    }
}
