package net.minepact.supernova.api.commands.arguments

import net.minepact.supernova.api.commands.arguments.types.ArgumentType

class Argument<T : Any>(
    val name: String,
    val raw: String,
    val value: T,
    val type: ArgumentType<T>,
)