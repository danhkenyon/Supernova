package net.minepact.supernova.api.commands.arguments.types.primative

import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.types.ArgumentType

object BooleanArgumentType : ArgumentType<Boolean> {
    override val name = "boolean"

    override fun parse(input: String, context: ArgumentContext) =
        when (input.lowercase()) {
            "true" -> true
            "false" -> false
            else -> null
        }

    override fun complete(input: String, context: ArgumentContext) =
        listOf("true", "false").filter {
            it.startsWith(input, ignoreCase = true)
        }
}