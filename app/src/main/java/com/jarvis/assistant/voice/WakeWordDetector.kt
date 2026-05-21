package com.jarvis.assistant.voice

class WakeWordDetector {
    private var wakeWord: String = "hey jarvis"
    fun setWakeWord(word: String) { wakeWord = word.lowercase().trim() }
    fun detect(text: String): Boolean = text.lowercase().trim().contains(wakeWord)
    fun extractCommand(text: String): String = text.lowercase().trim().removePrefix(wakeWord).trim()
}
