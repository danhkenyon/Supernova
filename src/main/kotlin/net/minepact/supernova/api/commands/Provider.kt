package net.minepact.api.command

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender

/**
 * A provider is a function that takes in a CommandSender and returns a list of potential values for an argument.
 *
 * @see net.minepact.api.command.arguments.ExpectedArgument
 *
 * @author dankenyon - 22/02/26
 */
class Provider {
    companion object {
        val EMPTY: (CommandSender) -> List<String> = { _ -> emptyList() }

        val PLAYERS: (CommandSender) -> List<String> = { _ -> Bukkit.getOnlinePlayers().map { it.name } }
        val WORLDS: (CommandSender) -> List<String> = { sender -> sender.server.worlds.map { it.name } }
        val GAME_MODES: (CommandSender) -> List<String> = { _ -> GameMode.entries.map { it.name.lowercase() } }
    }
}
