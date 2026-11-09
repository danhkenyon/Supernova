package net.minepact.supernova.api.commands.arguments.range

class FloatArgumentRange(
    override var min: Float = -Float.MAX_VALUE,
    override var max: Float = Float.MAX_VALUE
) : ArgumentRange<Float>