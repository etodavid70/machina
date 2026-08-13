package com.example.machina.terminals.linux

import android.content.Context
import com.termux.terminal.TerminalSessionClient
import com.termux.terminal.TerminalSession

/**
 * Minimal implementation of TerminalSessionClient for Machina.
 * Handles callbacks from the terminal session (process output, title changes, etc.)
 */
class MachinaTerminalSessionClient(
    private val context: Context,
    private val onSessionFinished: (TerminalSession) -> Unit = {}
) : TerminalSessionClient {
    
    override fun onTextChanged(changedSession: TerminalSession) {
        // Terminal screen updated - rendering will happen automatically
        // The TerminalView will handle invalidation and rendering
    }
    
    override fun onTitleChanged(changedSession: TerminalSession) {
        // Shell changed window title via escape sequence
        val title = changedSession.title
        println("Terminal title: $title")
    }
    
    override fun onSessionFinished(finishedSession: TerminalSession) {
        println("Terminal session finished with exit code: ${finishedSession.exitStatus}")
        onSessionFinished.invoke(finishedSession)
    }
    
    override fun onBell(session: TerminalSession) {
        // Shell sent bell character (BEL, ^G)
        println("Terminal bell!")
    }
    
    override fun onColorsChanged(session: TerminalSession) {
        // Terminal color palette changed
        println("Terminal colors changed")
    }
    
    override fun onCopyTextToClipboard(session: TerminalSession, text: String?) {
        // Shell copied text (OSC 52 escape sequence)
        if (!text.isNullOrEmpty()) {
            println("Copied to clipboard: $text")
        }
    }
    
    override fun onPasteTextFromClipboard(session: TerminalSession?) {
        // Shell requested paste from clipboard
        println("Paste from clipboard requested")
    }
    
    override fun getTerminalCursorStyle(): Int? {
        return null
    }
    
    override fun setTerminalShellPid(session: TerminalSession, pid: Int) {
        // Shell process PID
        println("Shell PID: $pid")
    }
    
    override fun onTerminalCursorStateChange(state: Boolean) {
        // Cursor visibility state changed
    }
    
    override fun logError(tag: String?, message: String?) {
        println("ERROR [$tag] $message")
    }
    
    override fun logWarn(tag: String?, message: String?) {
        println("WARN [$tag] $message")
    }
    
    override fun logInfo(tag: String?, message: String?) {
        println("INFO [$tag] $message")
    }
    
    override fun logDebug(tag: String?, message: String?) {
        println("DEBUG [$tag] $message")
    }
    
    override fun logVerbose(tag: String?, message: String?) {
        println("[$tag] $message")
    }
    
    override fun logStackTraceWithMessage(tag: String?, message: String?, e: Exception?) {
        println("ERROR [$tag] $message")
        e?.printStackTrace()
    }
    
    override fun logStackTrace(tag: String?, e: Exception?) {
        e?.printStackTrace()
    }
}
