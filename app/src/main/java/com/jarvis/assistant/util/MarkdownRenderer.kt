package com.jarvis.assistant.util

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon

object MarkdownRenderer {
    fun render(context: Context, textView: TextView, markdown: String) {
        val markwon = Markwon.create(context)
        markwon.setMarkdown(textView, markdown)
    }
}
