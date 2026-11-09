package net.minepact.supernova.api.commands.arguments.types.primative

import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.types.ArgumentType

object DoubleArgumentType : ArgumentType<Double> {
    override val name = "double"

    override fun parse(input: String, context: ArgumentContext) =
        input.toDoubleOrNull()?.takeIf { it.isFinite() }
}