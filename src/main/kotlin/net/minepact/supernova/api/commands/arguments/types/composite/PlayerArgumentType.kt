package net.minepact.supernova.api.commands.arguments.types.composite

import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.types.ArgumentType
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PlayerArgumentType : ArgumentType<Player> {
    override val name = "player"

    override fun parse(input: String, context: ArgumentContext) =
        Bukkit.getPlayerExact(input)

    override fun complete(input: String, context: ArgumentContext) =
        Bukkit.getOnlinePlayers()
            .map { it.name }
            .filter { it.startsWith(input, ignoreCase = true) }
}