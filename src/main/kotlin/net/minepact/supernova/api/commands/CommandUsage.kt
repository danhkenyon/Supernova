package net.minepact.supernova.api.commands

import net.minepact.supernova.api.commands.arguments.*
import org.bukkit.command.CommandSender

data class CommandUsage(
    val label: String,
    val arguments: List<ExpectedArgument<*>>
) {
    fun parse(
        sender: CommandSender,
        input: Array<String>
    ): ArgumentResult {
        val parsed = mutableListOf<Argument<*>>()
        val parsedByName = mutableMapOf<String, Argument<*>>()
        var inputIndex = 0

        for (expected in arguments) {
            val context = ArgumentContext(sender, parsedByName)
            val required = expected.optional

            if (expected.requiredWhen != null && !required) continue
            if (inputIndex >= input.size) {
                if (required) return ArgumentResult.Failure("Missing required argument: ${expected.name}")
                continue
            }

            if (expected.permission != null && !sender.hasPermission(expected.permission))
                return ArgumentResult.Failure("You do not have permission to use argument '${expected.name}'.")
            if (expected.senderFilter?.invoke(sender) == false)
                return ArgumentResult.Failure("You cannot use argument '${expected.name}'.")

            val raw = if (expected.consumeRemaining) input.drop(inputIndex).joinToString(" ")
            else input[inputIndex]
            val argument = ArgumentParser.parse(raw, expected, context)
                ?: return ArgumentResult.Failure("Invalid value for argument '${expected.name}': $raw")

            parsed.add(argument)
            parsedByName[expected.name] = argument

            inputIndex += if (expected.consumeRemaining) input.size - inputIndex
            else 1
        }

        if (inputIndex < input.size) return ArgumentResult.Failure("Too many arguments provided.")

        return ArgumentResult.Success(CommandArguments(parsed))
    }

    fun complete(
        sender: CommandSender,
        input: Array<String>
    ): List<String> {
        val currentIndex = input.size - 1
        if (currentIndex < 0) return emptyList()

        val parsedByName = mutableMapOf<String, Argument<*>>()
        var inputIndex = 0

        for (expected in arguments) {
            val context = ArgumentContext(sender, parsedByName)
            val required = expected.optional

            if (expected.requiredWhen != null && !required) continue
            if (inputIndex == currentIndex) {
                if (expected.permission != null && !sender.hasPermission(expected.permission)) return emptyList()
                if (expected.senderFilter?.invoke(sender) == false) return emptyList()

                val candidates = buildList {
                    addAll(expected.potentialValues.orEmpty())
                    addAll(expected.dynamicProvider?.invoke(sender).orEmpty())
                    addAll(expected.contextualProvider?.invoke(context).orEmpty())
                }
                return candidates
                    .distinct()
                    .filter { it.startsWith(input[currentIndex], ignoreCase = true) }
            }

            if (inputIndex >= currentIndex) return emptyList()
            val raw = if (expected.consumeRemaining) input.drop(inputIndex).joinToString(" ")
            else input[inputIndex]

            val argument = ArgumentParser.parse(
                raw,
                expected,
                context
            ) ?: return emptyList()

            parsedByName[expected.name] = argument

            inputIndex += if (expected.consumeRemaining) input.size - inputIndex
            else 1
        }

        return emptyList()
    }

    override fun toString(): String {
        return "/$label " + arguments.joinToString(" ") {
            if (it.consumeRemaining) if (it.optional) "[${it.name}...]" else "<${it.name}...>"
            else if (it.optional) "[${it.name}]" else "<${it.name}>"
        }
    }
}