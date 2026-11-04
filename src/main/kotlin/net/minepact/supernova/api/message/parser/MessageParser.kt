package net.minepact.supernova.api.message.parser

import net.minepact.supernova.api.message.MessageSegment
import net.minepact.supernova.api.message.action.MessageAction
import net.minepact.supernova.api.message.colour.*

class MessageParser {
    fun parse(
        input: String,
        actions: List<MessageAction> = emptyList()
    ): List<MessageSegment> {
        val characters = mutableListOf<StyledChar>()
        val styleStack = mutableListOf<MessageStyle>()
        val gradientStack = mutableListOf<ActiveGradient>()
        var style = MessageStyle()
        var i = 0

        fun setStyle(newStyle: MessageStyle) {
            style = newStyle
        }
        fun pushStyle() {
            styleStack += style
        }
        fun popStyle() {
            style = styleStack.removeLastOrNull() ?: MessageStyle()
        }
        fun applyGradient(startIndex: Int, endIndex: Int, gradient: GradientState) {
            val length = endIndex - startIndex
            if (length <= 0) return

            for (index in startIndex until endIndex) {
                val progress = if (length == 1) 0f
                else (index - startIndex).toFloat() / (length - 1)

                val colour = MessageColour.interpolateGradient(gradient.colours, progress)
                characters[index].style = characters[index].style.copy(color = colour)
            }
        }
        fun parseGradient(tag: String): GradientState? {
            if (!tag.startsWith("g:")) return null
            return try {
                val colours = tag
                    .removePrefix("g:")
                    .split(":")
                    .map { MessageColour.fromHex(it.removePrefix("#")) }

                if (colours.size < 2) return null
                GradientState(colours)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
        fun appendLegacyColor(code: Char): Boolean {
            val colour = LEGACY_COLORS[code.lowercaseChar()] ?: return false
            setStyle(style.copy(color = colour))
            return true
        }

        while (i < input.length) {
            if (input[i] == '&' && i + 1 < input.length) {
                val code = input[i + 1].lowercaseChar()
                if (appendLegacyColor(code)) {
                    i += 2
                    continue
                }

                val formatting = LEGACY_FORMATTING[code]

                if (formatting != null) {
                    setStyle(
                        style.copy(
                            bold = style.bold || formatting.bold,
                            italic = style.italic || formatting.italic,
                            underline = style.underline || formatting.underline,
                            strikethrough = style.strikethrough || formatting.strikethrough,
                            obfuscated = style.obfuscated || formatting.obfuscated
                        )
                    )

                    i += 2
                    continue
                }

                if (code == 'r') {
                    setStyle(MessageStyle())
                    i += 2
                    continue
                }
            }

            if (input.startsWith("&#", i) && i + 8 <= input.length) {
                try {
                    val hex = input.substring(i + 2, i + 8)
                    val colour = MessageColour.fromHex(hex)

                    setStyle(style.copy(color = colour))
                    i += 8
                    continue
                } catch (_: IllegalArgumentException) {}
            }

            if (input[i] == '<') {
                val closingIndex = input.indexOf('>', i)
                if (closingIndex != -1) {
                    val tag = input.substring(i + 1, closingIndex)

                    if (tag.startsWith("g:")) {
                        val gradient = parseGradient(tag)

                        if (gradient != null) {
                            gradientStack += ActiveGradient(
                                startIndex = characters.size,
                                gradient = gradient
                            )

                            i = closingIndex + 1
                            continue
                        }
                    }

                    if (tag == "/g") {
                        val active = gradientStack.removeLastOrNull()
                        if (active != null) {
                            applyGradient(
                                active.startIndex,
                                characters.size,
                                active.gradient
                            )

                            i = closingIndex + 1
                            continue
                        }
                    }

                    if (handleTag(tag, style, ::setStyle, ::pushStyle, ::popStyle)) {
                        i = closingIndex + 1
                        continue
                    }
                }
            }

            characters += StyledChar(
                char = input[i],
                style = style
            )

            i++
        }

        while (gradientStack.isNotEmpty()) {
            val active = gradientStack.removeLast()
            applyGradient(
                active.startIndex,
                characters.size,
                active.gradient
            )
        }

        val segments = mutableListOf<MessageSegment>()
        val text = StringBuilder()
        var currentStyle: MessageStyle? = null

        fun flush() {
            if (text.isEmpty()) return

            segments += MessageSegment(
                text = text.toString(),
                style = currentStyle!!,
                actions = actions
            )

            text.clear()
        }

        for (character in characters) {
            if (currentStyle != character.style) {
                flush()
                currentStyle = character.style
            }

            text.append(character.char)
        }

        flush()

        return segments
    }

    private fun handleTag(
        tag: String,
        currentStyle: MessageStyle,
        setStyle: (MessageStyle) -> Unit,
        pushStyle: () -> Unit,
        popStyle: () -> Unit
    ): Boolean {
        if (tag == "/") {
            popStyle()
            return true
        }
        if (tag == "reset") {
            setStyle(MessageStyle())
            return true
        }
        if (tag.startsWith("/")) {
            popStyle()
            return true
        }
        if (tag.startsWith("g:")) return false
        val colour = when {
            tag.startsWith("hex:") -> MessageColour.fromHex(tag.removePrefix("hex:"))
            tag.startsWith("#") -> MessageColour.fromHex(tag.removePrefix("#"))
            else -> NAMED_COLORS[tag.lowercase()]
        }

        if (colour != null) {
            pushStyle()
            setStyle(currentStyle.copy(color = colour))
            return true
        }
        val formatting = when (tag.lowercase()) {
            "bold" -> currentStyle.copy(bold = true)
            "italic" -> currentStyle.copy(italic = true)
            "underlined", "underline" -> currentStyle.copy(underline = true)
            "strikethrough" -> currentStyle.copy(strikethrough = true)
            "obfuscated" -> currentStyle.copy(obfuscated = true)
            else -> return false
        }

        pushStyle()
        setStyle(formatting)
        return true
    }

    companion object {
        private val NAMED_COLORS = mapOf(
            "black" to MessageColour(0, 0, 0),
            "dark_blue" to MessageColour(0, 0, 170),
            "dark_green" to MessageColour(0, 170, 0),
            "dark_aqua" to MessageColour(0, 170, 170),
            "dark_red" to MessageColour(170, 0, 0),
            "dark_purple" to MessageColour(170, 0, 170),
            "gold" to MessageColour(255, 170, 0),
            "gray" to MessageColour(170, 170, 170),
            "dark_gray" to MessageColour(85, 85, 85),
            "blue" to MessageColour(85, 85, 255),
            "green" to MessageColour(85, 255, 85),
            "aqua" to MessageColour(85, 255, 255),
            "red" to MessageColour(255, 85, 85),
            "light_purple" to MessageColour(255, 85, 255),
            "yellow" to MessageColour(255, 255, 85),
            "white" to MessageColour(255, 255, 255)
        )
        private val LEGACY_COLORS = mapOf(
            '0' to NAMED_COLORS.getValue("black"),
            '1' to NAMED_COLORS.getValue("dark_blue"),
            '2' to NAMED_COLORS.getValue("dark_green"),
            '3' to NAMED_COLORS.getValue("dark_aqua"),
            '4' to NAMED_COLORS.getValue("dark_red"),
            '5' to NAMED_COLORS.getValue("dark_purple"),
            '6' to NAMED_COLORS.getValue("gold"),
            '7' to NAMED_COLORS.getValue("gray"),
            '8' to NAMED_COLORS.getValue("dark_gray"),
            '9' to NAMED_COLORS.getValue("blue"),
            'a' to NAMED_COLORS.getValue("green"),
            'b' to NAMED_COLORS.getValue("aqua"),
            'c' to NAMED_COLORS.getValue("red"),
            'd' to NAMED_COLORS.getValue("light_purple"),
            'e' to NAMED_COLORS.getValue("yellow"),
            'f' to NAMED_COLORS.getValue("white")
        )
        private val LEGACY_FORMATTING = mapOf(
            'l' to LegacyFormatting(bold = true),
            'o' to LegacyFormatting(italic = true),
            'n' to LegacyFormatting(underline = true),
            'm' to LegacyFormatting(strikethrough = true),
            'k' to LegacyFormatting(obfuscated = true)
        )
    }
}