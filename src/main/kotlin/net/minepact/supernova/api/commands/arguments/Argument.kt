package net.minepact.supernova.api.commands.arguments

class Argument<T : Any>(
    val raw: String,
    val value: T,
    val type: ArgumentInputType,
)