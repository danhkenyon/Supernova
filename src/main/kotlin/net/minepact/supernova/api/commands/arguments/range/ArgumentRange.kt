package net.minepact.supernova.api.commands.arguments.range

sealed interface ArgumentRange<T : Number> {
    val min: T
    val max: T

    fun contains(value: T): Boolean =
        value.toDouble() >= min.toDouble() &&
                value.toDouble() <= max.toDouble()
}