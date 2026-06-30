package com.example.machina.terminal

import android.content.Context
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.concurrent.thread

/**
 * LocalShellSession - Interactive shell session using ProcessBuilder
 * Streams output directly to terminal emulator
 */
class LocalShellSession(
    private val context: Context,
    private val onOutput: (String) -> Unit,
    private val onError: (String) -> Unit,
    private val onClose: () -> Unit
) {
    private var process: Process? = null
    private var reader: BufferedReader? = null
    private var writer: BufferedWriter? = null
    private var isRunning = false

    fun start() {
        if (isRunning) return
        isRunning = true

        thread(isDaemon = false, name = "ShellSession") {
            try {
                val pb = ProcessBuilder("/system/bin/sh", "-i")
                pb.directory(context.filesDir)
                pb.environment().apply {
                    put("TERM", "xterm-256color")
                    put("HOME", context.filesDir.absolutePath)
                    put("PATH", "/system/bin:/system/xbin")
                    put("TMPDIR", context.cacheDir.absolutePath)
                    put("SHELL", "/system/bin/sh")
                }

                process = pb.redirectErrorStream(true).start()
                reader = BufferedReader(InputStreamReader(process!!.inputStream), 4096)
                writer = BufferedWriter(OutputStreamWriter(process!!.outputStream), 4096)

                // Initial shell prompt
                onOutput("$ ")

                // Read output continuously
                thread(isDaemon = true, name = "ShellReader") {
                    try {
                        val buffer = CharArray(512)
                        while (isRunning && process?.isAlive == true) {
                            try {
                                val count = reader!!.read(buffer, 0, buffer.size)
                                if (count > 0) {
                                    onOutput(String(buffer, 0, count))
                                } else if (count < 0) {
                                    break
                                }
                            } catch (e: Exception) {
                                if (isRunning) {
                                    onError("Read error: ${e.message}")
                                }
                                break
                            }
                        }
                    } catch (e: Exception) {
                        if (isRunning) onError("Reader thread error: ${e.message}")
                    }
                }

                // Wait for process
                val exitCode = process!!.waitFor()
                if (isRunning) {
                    onOutput("\n[Process exited with code $exitCode]\n")
                }
            } catch (e: Exception) {
                onError("Shell start error: ${e.message}\n")
            } finally {
                isRunning = false
                cleanup()
                onClose()
            }
        }
    }

    fun write(data: String) {
        if (!isRunning || writer == null) return
        try {
            writer!!.write(data)
            writer!!.flush()
        } catch (e: Exception) {
            onError("Write error: ${e.message}")
        }
    }

    fun write(bytes: ByteArray) {
        write(String(bytes))
    }

    fun terminate() {
        isRunning = false
        try {
            process?.destroy()
        } catch (e: Exception) {
            onError("Terminate error: ${e.message}")
        }
        cleanup()
    }

    private fun cleanup() {
        try {
            reader?.close()
            writer?.close()
            process?.destroy()
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
        reader = null
        writer = null
        process = null
    }

    fun isAlive(): Boolean = isRunning && process?.isAlive == true
}
