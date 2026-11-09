package net.minepact.supernova.api.commands.arguments.range

class IntegerArgumentRange(
    override var min: Int = -Int.MAX_VALUE,
    override var max: Int = Int.MAX_VALUE
) : ArgumentRange<Int>