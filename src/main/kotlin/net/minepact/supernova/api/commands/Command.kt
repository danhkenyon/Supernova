package net.minepact.supernova.api.commands

import net.minepact.api.command.Result
import net.minepact.supernova.api.commands.arguments.Argument
import net.minepact.supernova.api.permissions.Permission
import org.bukkit.command.CommandSender

abstract class Command(
    val name: String,
    val description: String,
    val permission: Permission,
    val aliases: List<String>,
    val usage: CommandUsage
) {
    abstract fun execute(sender: CommandSender, args: MutableList<Argument<*>>): Result
}