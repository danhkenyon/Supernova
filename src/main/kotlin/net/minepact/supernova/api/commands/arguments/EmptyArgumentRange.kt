package net.minepact.supernova.api.commands.arguments

class EmptyArgumentRange(
    override val min: Number = 0,
    override val max: Number = 0
) : ArgumentRange<Number>