package com.example.machina.data.repository

import com.example.machina.data.model.dashboard_models.SshConnectionRequest
import com.example.machina.data.model.dashboard_models.SshConnectionResult
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Properties

class SshConnectionRepository {

    fun connect(request: SshConnectionRequest): SshConnectionResult {
        var session: Session? = null

        try {
            val connectedSession = createSession(request)
            session = connectedSession
            connectedSession.connect(CONNECT_TIMEOUT_MS)

            val output = runCommand(
                session = connectedSession,
                command = "printf 'connected:%s@%s' \"$(whoami)\" \"$(hostname)\"",
                timeoutMs = COMMAND_TIMEOUT_MS
            ).output

            return SshConnectionResult(
                host = request.host.trim(),
                username = request.username.trim(),
                port = request.port,
                output = output.ifBlank { "SSH connection established." }
            )
        } catch (e: JSchException) {
            throw IllegalStateException(buildFailureMessage(request, e), e)
        } finally {
            if (session?.isConnected == true) {
                session.disconnect()
            }
        }
    }

    fun executeCommand(request: SshConnectionRequest, command: String): SshCommandResult {
        val trimmedCommand = command.trim()
        require(trimmedCommand.isNotBlank()) { "Enter a command to run." }

        var session: Session? = null

        try {
            val connectedSession = createSession(request)
            session = connectedSession
            connectedSession.connect(CONNECT_TIMEOUT_MS)
            return runCommand(connectedSession, trimmedCommand, COMMAND_TIMEOUT_MS)
        } catch (e: JSchException) {
            throw IllegalStateException(buildFailureMessage(request, e), e)
        } finally {
            if (session?.isConnected == true) {
                session.disconnect()
            }
        }
    }


    //Eto: function that calls createSession function just below this function
    // the createSession uses jsch library and returns session
    fun openShell(
        //request model
        request: SshConnectionRequest,
        columns: Int = DEFAULT_TERMINAL_COLUMNS,
        rows: Int = DEFAULT_TERMINAL_ROWS
    ): SshShellConnection {
        var session: Session? = null

        try {
            //Eto: createSession is called
            //it is defined below
            val connectedSession = createSession(request)
            session = connectedSession
            connectedSession.connect(CONNECT_TIMEOUT_MS)

            val channel = connectedSession.openChannel("shell") as ChannelShell
            channel.setPty(true)
            channel.setPtyType("xterm-256color")
            channel.setPtySize(columns, rows, 0, 0)

            //Eto: data coming from the server
            val input = channel.inputStream

            //Eto: Data going to the server
            val output = channel.outputStream

            channel.connect(CONNECT_TIMEOUT_MS)

            output.write("\n".toByteArray())
            output.flush()

            //Eto: this class is a created below
            return SshShellConnection(
                session = connectedSession,
                channel = channel,
                input = input,
                output = output
            )
        } catch (e: Exception) {
            if (session?.isConnected == true) {
                session.disconnect()
            }
            if (e is JSchException) {
                throw IllegalStateException(buildFailureMessage(request, e), e)
            }
            throw e
        }
    }


    //Eto: this is the function that creates session using jsch library
    //called in the openSession function
    private fun createSession(request: SshConnectionRequest): Session {
        val jsch = JSch()

        //Eto: for the pem key or password
        request.privateKey?.let { privateKey ->
            jsch.addIdentity(
                request.privateKeyName ?: "machina-uploaded-key",
                privateKey,
                null,
                request.password?.takeIf { it.isNotBlank() }?.toByteArray()
            )
        }

        //Eto: Starts a jsch session
        val session = jsch.getSession(request.username.trim(), request.host.trim(), request.port)
        request.password?.takeIf { it.isNotBlank() }?.let(session::setPassword)

        val config = Properties().apply {
            put("StrictHostKeyChecking", "no")

            //Eto: preferredAuthentications function is called
            //it is defined below
            put("PreferredAuthentications", preferredAuthentications(request))
        }

        session.setConfig(config)
        session.timeout = CONNECT_TIMEOUT_MS

        //returns a jsch.getSession
        return session
    }

    //Eto: function that ---
   //this is called in the createSession function
    private fun preferredAuthentications(request: SshConnectionRequest): String {
        return if (request.privateKey != null) {
            "publickey,password,keyboard-interactive"
        } else {
            "password,keyboard-interactive"
        }
    }

    private fun runCommand(
        session: Session,
        command: String,
        timeoutMs: Int
    ): SshCommandResult {
        val channel = session.openChannel("exec") as ChannelExec
        val output = ByteArrayOutputStream()
        val error = ByteArrayOutputStream()

        channel.setCommand(command)
        channel.inputStream = null
        channel.outputStream = output
        channel.setErrStream(error)

        try {
            channel.connect(timeoutMs)

            val deadline = System.currentTimeMillis() + timeoutMs
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

            return SshCommandResult(
                output = output.toString(Charsets.UTF_8.name()).trim(),
                error = errorMessage,
                exitStatus = channel.exitStatus
            )
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
                "SSH authentication failed. For EC2, check that the username matches the AMI (for example ubuntu or ec2-user) and that the PEM key is the key pair attached to this instance."
            }
            error.message?.contains("invalid privatekey", ignoreCase = true) == true ||
                error.message?.contains("invalid private key", ignoreCase = true) == true -> {
                "The selected PEM key could not be read. Upload the private .pem file for this EC2 key pair, not a .pub or .ppk file."
            }
            error.message?.contains("Connection refused", ignoreCase = true) == true -> {
                "Connection refused by ${request.host}:${request.port}. Check that SSH is running and listening on port ${request.port}."
            }
            error.message?.contains("Algorithm negotiation fail", ignoreCase = true) == true -> {
                "SSH algorithm negotiation failed. The server and app could not agree on a supported SSH key or host-key algorithm."
            }
            else -> error.message ?: "SSH connection failed"
        }
    }

    private companion object {
        const val CONNECT_TIMEOUT_MS = 15_000
        const val COMMAND_TIMEOUT_MS = 10_000
        const val DEFAULT_TERMINAL_COLUMNS = 80
        const val DEFAULT_TERMINAL_ROWS = 24
    }
}

data class SshCommandResult(
    val output: String,
    val error: String,
    val exitStatus: Int
)

//Eto: this is what is returned
class SshShellConnection(
    private val session: Session,
    private val channel: ChannelShell,
    val input: InputStream,   // SSH → Android
    val output: OutputStream   // Android → SSH
)
 {
    fun resize(columns: Int, rows: Int) {
        if (channel.isConnected) {
            channel.setPtySize(columns, rows, 0, 0)
        }
    }

    fun disconnect() {
        if (channel.isConnected) {
            channel.disconnect()
        }
        if (session.isConnected) {
            session.disconnect()
        }
    }
}
