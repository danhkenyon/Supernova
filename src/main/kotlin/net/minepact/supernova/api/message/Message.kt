package net.minepact.supernova.api.message

class Message internal constructor(val segments: List<MessageSegment>) {
    fun plainText(): String = segments.joinToString("") { it.text }
}