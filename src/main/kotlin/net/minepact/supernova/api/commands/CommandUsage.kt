package net.minepact.supernova.api.commands

import net.minepact.supernova.api.commands.arguments.ExpectedArgument

data class CommandUsage(
    val label: String,
    val arguments: List<ExpectedArgument>
) {
    override fun toString(): String {
        return "/$label " + arguments.joinToString(" ") {
            if (it.optional) "[${it.name}]"
            else "<${it.name}>"
        }
    }
}