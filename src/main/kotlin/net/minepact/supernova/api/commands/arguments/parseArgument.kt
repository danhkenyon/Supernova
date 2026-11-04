package net.minepact.supernova.api.commands.arguments

fun parseArgument(
    input: String,
    expected: ExpectedArgument
): Argument<*>? {
    val parsed = expected.inputType.parse(input) ?: return null
    when (val range = expected.range) {
        is IntegerArgumentRange -> {
            val value = parsed as Int
            if (value < range.min || value > range.max) return null
        }
        is DoubleArgumentRange -> {
            val value = parsed as Double
            if (value < range.min || value > range.max) return null
        }
        is FloatArgumentRange -> {
            val value = parsed as Float
            if (value < range.min || value > range.max) return null
        }
        is EmptyArgumentRange -> {}
    }

    return Argument(
        raw = input,
        value = parsed,
        type = expected.inputType
    )
}