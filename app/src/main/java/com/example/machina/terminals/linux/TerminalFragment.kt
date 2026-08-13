package com.example.machina.terminals.linux

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.termux.terminal.TerminalSession
import com.termux.view.TerminalView

/**
 * Fragment that hosts a Termux TerminalView for displaying and interacting with a shell session.
 * This is a minimal wrapper around TerminalView that manages the terminal session lifecycle.
 */
class TerminalFragment : Fragment() {
    
    private var terminalView: TerminalView? = null
    private var terminalSession: TerminalSession? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create TerminalView programmatically
        terminalView = TerminalView(requireContext(), null).apply {
            // Set font size (in sp)
            setTextSize(12)
            // Set the view client for handling user input
            setTerminalViewClient(MachinaTerminalViewClient(requireContext()))
        }
        
        // Create terminal session for shell execution
        terminalSession = TerminalSession(
            mShellPath = "/system/bin/bash",  // Shell binary path
            mCwd = "/data/local/tmp",         // Working directory
            mArgs = arrayOf(),                // Shell arguments
            mEnv = arrayOf(
                "HOME=/data/local/tmp",
                "TERM=xterm-256color",
                "PATH=/system/bin:/bin:/usr/bin",
                "LANG=en_US.UTF-8"
            ),
            mTranscriptRows = 2000,           // Scrollback buffer size
            client = MachinaTerminalSessionClient(requireContext()) { session ->
                // Called when session finishes
                onSessionFinished(session)
            }
        )
        
        // Attach session to view
        terminalSession?.let {
            terminalView?.attachSession(it)
        }
        
        return terminalView!!
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up terminal session on fragment destroy
        terminalSession?.finishIfRunning()
        terminalView = null
        terminalSession = null
    }
    
    override fun onStart() {
        super.onStart()
        println("TerminalFragment: onStart")
    }
    
    override fun onStop() {
        super.onStop()
        println("TerminalFragment: onStop")
    }
    
    /**
     * Send text input to the terminal session.
     * The text will be written to the shell's stdin.
     */
    fun sendInput(text: String) {
        terminalSession?.write(text)
    }
    
    /**
     * Send a complete command to the terminal session.
     * Appends a newline character to execute the command.
     */
    fun sendCommand(command: String) {
        terminalSession?.write("$command\n")
    }
    
    /**
     * Get the current terminal session, if available.
     */
    fun getCurrentSession(): TerminalSession? {
        return terminalSession
    }
    
    /**
     * Get the terminal view, if available.
     */
    fun getTerminalView(): TerminalView? {
        return terminalView
    }
    
    /**
     * Called when the terminal session finishes (shell exits).
     */
    private fun onSessionFinished(session: TerminalSession) {
        println("Terminal session finished")
        // You could restart the shell, show a message, etc.
    }
    
    companion object {
        /**
         * Factory method to create a new TerminalFragment instance.
         */
        fun newInstance() = TerminalFragment()
    }
}
