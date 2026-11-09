package net.minepact.supernova.core.global.commands

import net.minepact.supernova.api.commands.*
import net.minepact.supernova.api.commands.arguments.*
import net.minepact.supernova.api.commands.arguments.types.primative.StringArgumentType
import net.minepact.supernova.api.items.ItemRegister
import net.minepact.supernova.api.message.action.Action
import net.minepact.supernova.api.message.helpers.Messages.ERROR_PREFIX
import net.minepact.supernova.api.message.helpers.Messages.NEXUS_PREFIX
import net.minepact.supernova.api.message.sendMessage
import net.minepact.supernova.api.permissions.Permission
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ItemCommand : Command(
    name = "item",
    description = "",
    permission = Permission("nexus.item.give"),
    aliases = listOf("i", "give-custom"),
    playerOnly = true,
    usage = CommandUsage(label = "item",
        arguments = listOf(
            ExpectedArgument(
                name = "name",
                type = StringArgumentType,
                optional = false,
                potentialValues = ItemRegister.all().map { it.id }
            )
        )
    )
) {
    override fun execute(sender: CommandSender, args: CommandArguments): CommandResult {
        assert(sender is Player)
        val player = sender as Player
        val customItem = ItemRegister.get(args.get<String>("name"))

        if (customItem == null) {
            sender.sendMessage(ERROR_PREFIX
                .append("<red>Could not find that item.") {
                    action(Action.SHOW_TEXT,
                        "<white>${args.get<String>("name")}<red> is not an item that is currently in the plugins registry.\n"+
                        "<red>Please contact a developer or check the items.yml config."
                    )
                }.build()
            )
            return CommandResult.FAILURE
        }

        player.inventory.addItem(customItem.asItemStack())
        sender.sendMessage(NEXUS_PREFIX
            .append("<green>Gave <white>${customItem.name}</white> to <white>${player.name}</white>.")
            .build()
        )
        return CommandResult.SUCCESS
    }
}