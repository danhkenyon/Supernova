package net.minepact.supernova.api.commands.arguments.types

import net.minepact.supernova.api.commands.arguments.ArgumentContext

interface ArgumentType<T : Any> {
    val name: String

    fun parse(
        input: String,
        context: ArgumentContext
    ): T?

    fun complete(
        input: String,
        context: ArgumentContext
    ): List<String> = emptyList()
}