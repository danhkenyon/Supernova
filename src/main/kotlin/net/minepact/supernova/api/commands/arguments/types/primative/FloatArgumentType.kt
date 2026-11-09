package net.minepact.supernova.api.commands.arguments.types.primative

import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.types.ArgumentType

object FloatArgumentType : ArgumentType<Float> {
    override val name = "float"

    override fun parse(input: String, context: ArgumentContext) =
        input.toFloatOrNull()?.takeIf { it.isFinite() }
}