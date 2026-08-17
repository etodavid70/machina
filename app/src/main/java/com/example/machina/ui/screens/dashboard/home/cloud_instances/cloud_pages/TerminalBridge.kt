package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import android.os.Build
import android.webkit.WebView
import androidx.annotation.RequiresApi
import com.example.machina.data.repository.SshShellConnection
import org.json.JSONObject
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong


class TerminalBridge(
    private val shell: SshShellConnection,
    private val webView: WebView
) {

    private var running = AtomicBoolean(true)
    private val outputQueue = LinkedBlockingQueue<String>(512)  // Larger queue for more buffering
    private var readerThread: Thread? = null
    private var flushThread: Thread? = null
    
    // Adaptive batching parameters (tuned for interactive response + throughput)
    private val BATCH_TIMEOUT_MS = 8L        // 8ms = 125fps (interactive feedback)
    private val MAX_BATCH_SIZE = 5           // Smaller batches = faster response
    private val ADAPTIVE_THRESHOLD = 50      // Switch to larger batches at 50 items
    private val LARGE_BATCH_SIZE = 20        // Larger batches for bulk output
    
    private val lastFlushTime = AtomicLong(System.currentTimeMillis())
    private val queueDepth = AtomicLong(0)


    //Eto: function to start the terminal
    //called in the launch  effect of the TerminalScreen
    fun start() {

        //Eto: SSH Reader thread: Continuously running
        readerThread = Thread {
            try {
                val buffer = ByteArray(8192)  // Larger buffer for fewer read() syscalls
                val input = shell.input

                while (running.get()) {
                    try {

                        //Eto: Wait until the SSH server sends me something.
                        val read = input.read(buffer)

                        if (read > 0) {
                            val data = String(buffer, 0, read, Charsets.UTF_8)
                            outputQueue.offer(data)
                            queueDepth.incrementAndGet()
                        } else if (read == -1) {
                            // EOF: stream closed
                            break
                        }
                    } catch (e: Exception) {
                        if (running.get()) {
                            e.printStackTrace()
                        }
                        break
                    }
                }
            } catch (e: Exception) {
                if (running.get()) {
                    e.printStackTrace()
                }
            }
        }.apply {
            name = "SSH-Reader"
            priority = Thread.NORM_PRIORITY + 1  // Slightly elevated priority
            start()
        }

        // Eto: Flush thread: Batch queue items and send to WebView (debounced)
        flushThread = Thread {
            try {
                val batch = StringBuilder(65536)  // Pre-allocate 64KB
                var itemCount = 0
                var lastFlush = System.currentTimeMillis()

                while (running.get()) {
                    // Poll with timeout
                    val item = outputQueue.poll(BATCH_TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS)

                    if (item != null) {
                        batch.append(item)
                        itemCount++
                        queueDepth.decrementAndGet()
                    }

                    val now = System.currentTimeMillis()
                    val timeSinceFlush = now - lastFlush
                    val currentQueueDepth = queueDepth.get()

                    // Decide batch size based on queue pressure
                    val batchLimit = if (currentQueueDepth > ADAPTIVE_THRESHOLD) LARGE_BATCH_SIZE else MAX_BATCH_SIZE

                    // Flush when: batch reached, timeout expired, or queue empty with data
                    val shouldFlush = itemCount >= batchLimit ||
                            (timeSinceFlush >= BATCH_TIMEOUT_MS && batch.isNotEmpty()) ||
                            (item == null && batch.isNotEmpty())

                    if (shouldFlush && batch.isNotEmpty()) {

                        //Eto: sendToTerminal is called
                        sendToTerminal(batch.toString())
                        batch.clear()
                        itemCount = 0
                        lastFlush = now
                        lastFlushTime.set(now)
                    }
                }
            } catch (e: Exception) {
                if (running.get()) {
                    e.printStackTrace()
                }
            }
        }.apply {
            name = "SSH-Flush"
            priority = Thread.NORM_PRIORITY
            start()
        }
    }


     //Eto: Sends user input from xterm.js to through webview to SSH
    //called in the AndroidView widget in the TerminalScreen
    fun sendInput(data: String) {
        try {
            // Eto: Write directly to SSH
            shell.output.write(data.toByteArray(Charsets.UTF_8))
            shell.output.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //Eto: Send from SSH output to xterm.js terminal(display)
     //called in the flushThread of the start function
    private fun sendToTerminal(data: String) {
        webView.post {
            try {
                // Single JSON quote operation for entire batch
                val escaped = JSONObject.quote(data)
                
                // Use evaluateJavascript for better performance than loadUrl
                webView.evaluateJavascript("window.writeToTerminal($escaped);", null)
            } catch (e: Exception) {
                if (running.get()) {
                    e.printStackTrace()
                }
            }
        }
    }


    //Eto: to stop any existing bridge
    //called in the DisposableEffect|onDispose function in the TerminalScreen
    fun stop() {
        running.set(false)
        try {
            readerThread?.join(500)
            flushThread?.join(500)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
        
        // Force interrupt if still alive
        readerThread?.interrupt()
        flushThread?.interrupt()
    }
}
