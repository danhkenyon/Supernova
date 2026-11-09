package net.minepact.supernova.api.commands.arguments.types.primative

import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.types.ArgumentType

object IntegerArgumentType : ArgumentType<Int> {
    override val name = "integer"

    override fun parse(input: String, context: ArgumentContext) = input.toIntOrNull()
}