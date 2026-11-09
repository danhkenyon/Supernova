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
import net.minepact.supernova.core.hub.menus.ExampleMenu
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
        /*
        sender.sendMessage(
            MessageBuilder()
                .append("\n<g:#000000:#ffffff><bold>---------------------------------------------</g>\n")
                .append("<hex:#abcdef>Sending you to <white>${args.get<String>("name")}</white>!\n")
                .append("<yellow><underline>Click here for free v-bucks!\n") {
                    action(Action.SHOW_TEXT,
                                "<g:#ff0000:#00ff00:#0000ff>Rainbow hover text!</g>\n" +
                                "<bold><#ffff00>Bold yellow text</#ffff00></bold>\n" +
                                "<italic><#ff00ff>Italic pink text</#ff00ff></italic>"
                    )
                    action(Action.SUGGEST_COMMAND, args.get<String>("name"))
                }
                .append("<g:#000000:#ffffff><bold>---------------------------------------------</g>\n")
                .build()
        )

         */

        MenuManager.open(
            sender as? Player ?: throw IllegalArgumentException("Player is not an instance of server command."),
            menu = ExampleMenu()
        )
        return CommandResult.SUCCESS
    }
}