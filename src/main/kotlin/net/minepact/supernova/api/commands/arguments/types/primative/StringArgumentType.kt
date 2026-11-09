package net.minepact.supernova.api.commands.arguments.types.primative

import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.types.ArgumentType

object StringArgumentType : ArgumentType<String> {
    override val name = "string"

    override fun parse(input: String, context: ArgumentContext) = input
}