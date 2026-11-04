package net.minepact.supernova.api.message.parser

data class LegacyFormatting(
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val strikethrough: Boolean = false,
    val obfuscated: Boolean = false
)