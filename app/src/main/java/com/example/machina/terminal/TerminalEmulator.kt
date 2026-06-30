package com.example.machina.terminal

/**
 * Simple ANSI terminal emulator
 * Handles basic terminal control sequences and screen rendering
 */
class TerminalEmulator(
    val cols: Int = 80,
    val rows: Int = 24
) {
    private val screenBuffer = mutableListOf<TerminalLine>()
    private var cursorX = 0
    private var cursorY = 0
    private var scrollTop = 0
    private var foregroundColor = 37 // white
    private var backgroundColor = 40 // black
    private var boldMode = false

    data class TerminalLine(
        val chars: MutableList<TerminalChar> = mutableListOf(),
        var dirty: Boolean = true
    )

    data class TerminalChar(
        val char: Char = ' ',
        val foreground: Int = 37,
        val background: Int = 40,
        val bold: Boolean = false
    )

    init {
        // Initialize screen buffer
        repeat(rows) {
            screenBuffer.add(TerminalLine())
        }
    }

    fun write(data: String) {
        data.forEach { char ->
            when {
                char == '\n' -> {
                    cursorX = 0
                    cursorY++
                    if (cursorY >= rows) {
                        scroll()
                        cursorY = rows - 1
                    }
                }
                char == '\r' -> {
                    cursorX = 0
                }
                char == '\u001B' -> {
                    // Start of escape sequence - handle later
                }
                char.code >= 32 -> {
                    // Printable character
                    ensureLineCapacity(cursorY)
                    val line = screenBuffer[cursorY]
                    
                    while (line.chars.size <= cursorX) {
                        line.chars.add(TerminalChar())
                    }
                    
                    line.chars[cursorX] = TerminalChar(
                        char = char,
                        foreground = foregroundColor,
                        background = backgroundColor,
                        bold = boldMode
                    )
                    line.dirty = true
                    
                    cursorX++
                    if (cursorX >= cols) {
                        cursorX = 0
                        cursorY++
                        if (cursorY >= rows) {
                            scroll()
                            cursorY = rows - 1
                        }
                    }
                }
            }
        }
    }

    fun getScreenBuffer(): List<TerminalLine> = screenBuffer

    fun getCursorPosition(): Pair<Int, Int> = cursorX to cursorY

    private fun ensureLineCapacity(lineIndex: Int) {
        while (screenBuffer.size <= lineIndex) {
            screenBuffer.add(TerminalLine())
        }
    }

    private fun scroll() {
        if (screenBuffer.size > rows) {
            screenBuffer.removeAt(0)
        } else {
            screenBuffer.add(TerminalLine())
        }
    }

    fun clear() {
        screenBuffer.clear()
        repeat(rows) {
            screenBuffer.add(TerminalLine())
        }
        cursorX = 0
        cursorY = 0
    }
}
