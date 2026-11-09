package net.minepact.supernova.api.commands.arguments.range

class DoubleArgumentRange(
    override var min: Double = -Double.MAX_VALUE,
    override var max: Double = Double.MAX_VALUE
) : ArgumentRange<Double>