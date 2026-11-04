package net.minepact.supernova.api.commands.arguments

import org.bukkit.command.CommandSender

open class ExpectedArgument(
    val name: String,
    var potentialValues: List<String>? = null,
    var inputType: ArgumentInputType = ArgumentInputType.STRING,
    var optional: Boolean = false,
    var range: ArgumentRange<*> = EmptyArgumentRange(),

    val permission: String? = null,
    val senderFilter: ((CommandSender) -> Boolean)? = null,
    val dynamicProvider: ((CommandSender) -> List<String>)? = null,
    val consumeRemaining: Boolean = false
)
