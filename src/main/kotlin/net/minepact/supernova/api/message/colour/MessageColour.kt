package net.minepact.supernova.api.message.colour

data class MessageColour(
    val red: Int,
    val green: Int,
    val blue: Int
) {
    init {
        require(red in 0..255)
        require(green in 0..255)
        require(blue in 0..255)
    }

    fun toHex(): String = "#%02X%02X%02X".format(red, green, blue)

    companion object {
        fun fromHex(hex: String): MessageColour {
            val value = hex.removePrefix("#")
            require(value.matches(Regex("[0-9a-fA-F]{6}"))) { "Invalid RGB colour: $hex" }

            return MessageColour(
                value.take(2).toInt(16),
                value.substring(2, 4).toInt(16),
                value.substring(4, 6).toInt(16)
            )
        }
        fun interpolate(start: MessageColour, end: MessageColour, progress: Double): MessageColour {
            val t = progress.coerceIn(0.0, 1.0)
            return MessageColour(
                (start.red + (end.red - start.red) * t).toInt(),
                (start.green + (end.green - start.green) * t).toInt(),
                (start.blue + (end.blue - start.blue) * t).toInt()
            )
        }
        fun interpolateGradient(colours: List<MessageColour>, progress: Float): MessageColour {
            if (colours.size == 1) return colours.first()

            val scaled = progress.coerceIn(0f, 1f) * (colours.size - 1)
            val index = scaled.toInt().coerceAtMost(colours.size - 2)
            val localProgress = scaled - index

            val start = colours[index]
            val end = colours[index + 1]

            return MessageColour(
                (start.red + (end.red - start.red) * localProgress).toInt(),
                (start.green + (end.green - start.green) * localProgress).toInt(),
                (start.blue + (end.blue - start.blue) * localProgress).toInt()
            )
        }

        val WHITE = MessageColour(255, 255, 255)
        val RED = MessageColour(255, 85, 85)
        val GREEN = MessageColour(85, 255, 85)
        val BLUE = MessageColour(85, 85, 255)
        val YELLOW = MessageColour(255, 255, 85)
    }
}