package net.minepact.supernova.api.commands.arguments.types.primative

import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.types.ArgumentType

object LongArgumentType : ArgumentType<Long> {
    override val name = "long"

    override fun parse(input: String, context: ArgumentContext) = input.toLongOrNull()
}