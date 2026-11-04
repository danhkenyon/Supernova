package net.minepact.supernova.api.commands.arguments

import org.bukkit.GameMode

enum class ArgumentInputType {
    INTEGER,
    STRING,
    BOOLEAN,
    DOUBLE,
    FLOAT,
    LONG,
    PLAYER,
    GAME_MODE;

    fun parse(input: String): Any? = try {
        when (this) {
            STRING -> input
            INTEGER -> input.toInt()
            DOUBLE -> input.toDouble()
            FLOAT -> input.toFloat()
            LONG -> input.toLong()
            BOOLEAN -> when {
                input.equals("true", true) -> true
                input.equals("false", true) -> false
                else -> null
            }
            PLAYER -> input // TODO
            GAME_MODE -> GameMode.valueOf(input.uppercase())
        }
    } catch (e: Exception) { null }
}