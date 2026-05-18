package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import android.webkit.WebView
import com.example.machina.data.repository.SshShellConnection
import org.json.JSONObject

class TerminalBridge(
    private val shell: SshShellConnection,
    private val webView: WebView
) {

    private var running = true

    fun start() {
        Thread {
            try {
                val buffer = ByteArray(4096)
                val input = shell.input   // SSH → Android

                while (running) {
                    val read = input.read(buffer)
                    if (read > 0) {
                        val data = String(buffer, 0, read)

                        webView.post {
                            sendToTerminal(data)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun sendInput(data: String) {
        shell.output.write(data.toByteArray())  // Android → SSH
        shell.output.flush()
    }

    private fun sendToTerminal(data: String) {
        val escaped = JSONObject.quote(data)


        webView.evaluateJavascript(
            "window.writeToTerminal($escaped);",
            null
        )
    }

    fun stop() {
        running = false
    }
}


