package net.minepact.supernova.api.commands.arguments

import net.minepact.supernova.api.commands.arguments.types.composite.PlayerArgumentType
import org.bukkit.entity.Player

object Arguments {
    val PLAYERS_REQUIRED: ExpectedArgument<Player> = ExpectedArgument(
        name = "players",
        type = PlayerArgumentType,
        dynamicProvider = Provider.PLAYERS,
        optional = false,
    )
    val PLAYERS_OPTIONAL: ExpectedArgument<Player> = ExpectedArgument(
        name = "players",
        type = PlayerArgumentType,
        dynamicProvider = Provider.PLAYERS,
        optional = true,
    )
}