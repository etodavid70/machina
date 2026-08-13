package com.example.machina.terminals.linux

import android.util.Log

/**
 * JNI Library loader for Termux terminal components.
 * Handles loading native libraries required for PTY operations.
 */
object TermuxNativeLibraryLoader {
    private const val LOG_TAG = "TermuxNativeLoader"
    
    /**
     * Load all required Termux native libraries.
     * Call this before creating any TerminalSession.
     */
    fun loadNativeLibraries() {
        try {
            // Try to load the main Termux library
            System.loadLibrary("termux")
            Log.d(LOG_TAG, "Successfully loaded libtermux.so")
        } catch (e: UnsatisfiedLinkError) {
            Log.w(LOG_TAG, "Failed to load libtermux.so: ${e.message}")
            // Try alternative names
            try {
                System.loadLibrary("termux-shared")
                Log.d(LOG_TAG, "Successfully loaded libtermux-shared.so")
            } catch (e2: UnsatisfiedLinkError) {
                Log.e(LOG_TAG, "Failed to load both libtermux.so and libtermux-shared.so")
                Log.e(LOG_TAG, "This is expected on non-Termux devices. Terminal will not work with native shells.")
            }
        }
    }
}
