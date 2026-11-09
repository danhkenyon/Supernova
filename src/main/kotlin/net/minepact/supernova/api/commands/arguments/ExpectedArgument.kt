package net.minepact.supernova.api.commands.arguments

import net.minepact.supernova.api.commands.arguments.range.ArgumentRange
import net.minepact.supernova.api.commands.arguments.types.ArgumentType
import org.bukkit.command.CommandSender

class ExpectedArgument<T : Any>(
    val name: String,
    val type: ArgumentType<T>,
    val optional: Boolean = false,
    val range: ArgumentRange<*>? = null,
    val permission: String? = null,
    val senderFilter: ((CommandSender) -> Boolean)? = null,
    val potentialValues: List<String>? = null,
    val dynamicProvider: ((CommandSender) -> List<String>)? = null,
    val contextualProvider: ((ArgumentContext) -> List<String>)? = null,
    val consumeRemaining: Boolean = false,
    val requiredWhen: ((ArgumentContext) -> Boolean)? = null,
    val validator: ((ArgumentContext, T) -> Boolean)? = null
)