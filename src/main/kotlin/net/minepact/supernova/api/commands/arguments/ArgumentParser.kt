package net.minepact.supernova.api.commands.arguments

import net.minepact.supernova.api.commands.arguments.range.ArgumentRange

object ArgumentParser {

    fun <T : Any> parse(
        input: String,
        expected: ExpectedArgument<T>,
        context: ArgumentContext
    ): Argument<T>? {
        val value = expected.type.parse(input, context)
            ?: return null

        val range = expected.range

        if (range != null) {
            if (value !is Number || !isWithinRange(value, range)) {
                return null
            }
        }

        if (expected.validator?.invoke(context, value) == false) {
            return null
        }

        return Argument(
            name = expected.name,
            raw = input,
            value = value,
            type = expected.type
        )
    }

    private fun isWithinRange(
        value: Number,
        range: ArgumentRange<*>
    ): Boolean {
        val actual = value.toDouble()
        return actual.isFinite() &&
                actual >= range.min.toDouble() &&
                actual <= range.max.toDouble()
    }
}