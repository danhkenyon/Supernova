package net.minepact.supernova.api.commands.arguments

sealed interface ArgumentRange<T : Number> {
    val min: T
    val max: T
}