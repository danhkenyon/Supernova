package net.minepact.supernova.api.message.colour

data class MessageStyle(
    val color: MessageColour? = null,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val strikethrough: Boolean = false,
    val obfuscated: Boolean = false
)