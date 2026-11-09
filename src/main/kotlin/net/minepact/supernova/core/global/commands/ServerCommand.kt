package net.minepact.supernova.core.global.commands

import net.minepact.supernova.api.commands.Command
import net.minepact.supernova.api.commands.CommandResult
import net.minepact.supernova.api.commands.CommandUsage
import net.minepact.supernova.api.commands.arguments.CommandArguments
import net.minepact.supernova.api.commands.arguments.ExpectedArgument
import net.minepact.supernova.api.commands.arguments.types.primative.StringArgumentType
import net.minepact.supernova.api.menus.MenuManager
import net.minepact.supernova.api.message.MessageBuilder
import net.minepact.supernova.api.message.action.Action
import net.minepact.supernova.api.message.sendMessage
import net.minepact.supernova.api.permissions.Permission
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ServerCommand : Command(
    name = "server",
    description = "Server command",
    permission = Permission("nexus.server.transfer.self"),
    aliases = listOf("goto"),
    usage = CommandUsage(label = "server",
        arguments = listOf(
            ExpectedArgument(
                name = "name",
                type = StringArgumentType,
                optional = false,
                potentialValues = listOf("hub1", "hub2", "box")
            )
        )
    )
) {
    override fun execute(sender: CommandSender, args: CommandArguments): CommandResult {
        return CommandResult.SUCCESS
    }
}