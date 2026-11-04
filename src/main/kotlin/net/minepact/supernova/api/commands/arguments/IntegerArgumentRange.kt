package net.minepact.supernova.api.commands.arguments

class IntegerArgumentRange(
    override var min: Int = Int.MIN_VALUE,
    override var max: Int = Int.MAX_VALUE
) : ArgumentRange<Int>