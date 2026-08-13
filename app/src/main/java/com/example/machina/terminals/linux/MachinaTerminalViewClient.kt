package com.example.machina.terminals.linux

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import com.termux.terminal.TerminalSession
import com.termux.view.TerminalViewClient

/**
 * Minimal implementation of TerminalViewClient for Machina.
 * Handles user input callbacks (keyboard, touch, etc.)
 * Optimized for performance: uses non-blocking async Log instead of println().
 */
class MachinaTerminalViewClient(
    private val context: Context
) : TerminalViewClient {
    
    private val TAG = "MachinaTerminalViewClient"
    
    override fun onSingleTapUp(e: MotionEvent) {
        // User tapped terminal - allow default behavior
    }
    
    override fun onScale(scale: Float): Float {
        // Pinch-zoom gesture - clamp to reasonable range (0.5x to 3x)
        return scale.coerceIn(0.5f, 3f)
    }
    
    override fun onLongPress(event: MotionEvent): Boolean {
        // Long press - could show context menu, allow default behavior
        return false
    }
    
    override fun copyModeChanged(copyMode: Boolean) {
        // Text selection mode toggled - async logging, non-blocking
        Log.v(TAG, "Copy mode: ${if (copyMode) "enabled" else "disabled"}")
    }
    
    override fun onKeyDown(keyCode: Int, e: KeyEvent, session: TerminalSession): Boolean {
        // Hardware key pressed - allow default behavior
        return false
    }
    
    override fun onKeyUp(keyCode: Int, e: KeyEvent): Boolean {
        // Hardware key released
        return false
    }
    
    override fun onCodePoint(codePoint: Int, ctrlDown: Boolean, session: TerminalSession): Boolean {
        // User entered Unicode code point - allow default behavior
        return false
    }
    
    override fun readControlKey(): Boolean = false
    override fun readAltKey(): Boolean = false
    override fun readShiftKey(): Boolean = false
    override fun readFnKey(): Boolean = false
    
    override fun shouldBackButtonBeMappedToEscape(): Boolean = false
    override fun shouldUseCtrlSpaceWorkaround(): Boolean = false
    override fun shouldEnforceCharBasedInput(): Boolean = false
    override fun isTerminalViewSelected(): Boolean = true
    
    override fun onEmulatorSet() {
        Log.v(TAG, "Terminal emulator set")
    }
    
    override fun logError(tag: String?, message: String?) {
        Log.e(TAG, "[$tag] $message")
    }
    
    override fun logWarn(tag: String?, message: String?) {
        Log.w(TAG, "[$tag] $message")
    }
    
    override fun logInfo(tag: String?, message: String?) {
        Log.i(TAG, "[$tag] $message")
    }
    
    override fun logDebug(tag: String?, message: String?) {
        Log.d(TAG, "[$tag] $message")
    }
    
    override fun logVerbose(tag: String?, message: String?) {
        Log.v(TAG, "[$tag] $message")
    }
    
    override fun logStackTraceWithMessage(tag: String?, message: String?, e: Exception?) {
        Log.e(TAG, "[$tag] $message", e)
    }
    
    override fun logStackTrace(tag: String?, e: Exception?) {
        Log.e(TAG, "Stack trace", e)
    }
}
