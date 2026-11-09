package net.minepact.supernova.api.commands

import net.minepact.supernova.api.commands.arguments.CommandArguments
import net.minepact.supernova.api.permissions.Permission
import net.minepact.supernova.api.server.ServerType
import org.bukkit.command.CommandSender

abstract class Command(
    val server: ServerType = ServerType.GLOBAL,
    val name: String,
    val description: String = "Default command description.",
    val permission: Permission = Permission("nexus.default"),
    val aliases: List<String> = listOf(),
    val cooldown: Double = -1.0,
    val playerOnly: Boolean = false,
    val usage: CommandUsage
) {
    abstract fun execute(sender: CommandSender, args: CommandArguments): CommandResult
}