package com.example.machina.terminals.linux

import android.content.Context
import com.termux.terminal.TerminalSession
import android.util.Log

/**
 * Safe terminal session creator that handles missing native libraries.
 * Falls back to showing a mock terminal if native library is unavailable.
 */
object SafeTerminalSessionFactory {
    private const val LOG_TAG = "SafeTerminalFactory"
    
    /**
     * Try to create a real terminal session.
     * Returns a session or null if native library is unavailable.
     */
    fun tryCreateSession(
        context: Context,
        shellPath: String = "/system/bin/bash",
        cwd: String = "/data/local/tmp",
        args: Array<String> = arrayOf(),
        env: Array<String> = arrayOf(
            "HOME=/data/local/tmp",
            "TERM=xterm-256color",
            "PATH=/system/bin:/bin:/usr/bin"
        ),
        transcriptRows: Int = 2000,
        onSessionFinished: (TerminalSession) -> Unit = {}
    ): TerminalSession? {
        return try {
            TerminalSession(
                mShellPath = shellPath,
                mCwd = cwd,
                mArgs = args,
                mEnv = env,
                mTranscriptRows = transcriptRows,
                client = MachinaTerminalSessionClient(context, onSessionFinished)
            )
        } catch (e: UnsatisfiedLinkError) {
            Log.e(LOG_TAG, "Native library not available: ${e.message}")
            Log.i(LOG_TAG, "Terminal will show as empty (native PTY not available on this device)")
            null
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to create terminal session: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Check if native library is available.
     */
    fun isNativeLibraryAvailable(): Boolean {
        return try {
            System.loadLibrary("termux")
            true
        } catch (e: UnsatisfiedLinkError) {
            try {
                System.loadLibrary("termux-shared")
                true
            } catch (e2: UnsatisfiedLinkError) {
                false
            }
        }
    }
}
