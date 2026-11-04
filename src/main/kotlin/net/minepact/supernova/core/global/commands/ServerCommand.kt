package net.minepact.supernova.core.global.commands

import net.kyori.adventure.text.Component
import net.minepact.api.command.Result
import net.minepact.supernova.api.commands.Command
import net.minepact.supernova.api.commands.CommandUsage
import net.minepact.supernova.api.commands.arguments.Argument
import net.minepact.supernova.api.commands.arguments.ExpectedArgument
import net.minepact.supernova.api.message.MessageBuilder
import net.minepact.supernova.api.message.action.Action
import net.minepact.supernova.api.message.sendMessage
import net.minepact.supernova.api.permissions.Permission
import org.bukkit.command.CommandSender

class ServerCommand : Command(
    name = "server",
    description = "Server command",
    permission = Permission("nexus.server"),
    aliases = listOf("goto"),
    usage = CommandUsage(label = "server",
        arguments = listOf(
            ExpectedArgument("name", listOf("hub1", "hub2", "box"))
        )
    )

) {
    override fun execute(sender: CommandSender, args: MutableList<Argument<*>>): Result {
        sender.sendMessage(
            MessageBuilder()
                .append("\n<g:#000000:#ffffff><bold>---------------------------------------------</g>\n")
                .append("<hex:#abcdef>Sending you to <white>${args[0].value}</white>!\n")
                .append("<yellow><underline>Click here for free v-bucks!\n") {
                    action(Action.SHOW_TEXT,
                                "<g:#ff0000:#00ff00:#0000ff>Rainbow hover text!</g>\n" +
                                "<bold><#ffff00>Bold yellow text</#ffff00></bold>\n" +
                                "<italic><#ff00ff>Italic pink text</#ff00ff></italic>"
                    )
                    action(Action.SUGGEST_COMMAND, args[0].value.toString())
                }
                .append("<g:#000000:#ffffff><bold>---------------------------------------------</g>\n")
                .build()
        )
        return Result.SUCCESS
    }
}