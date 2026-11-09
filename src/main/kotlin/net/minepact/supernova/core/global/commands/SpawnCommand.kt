package net.minepact.supernova.core.global.commands

import net.minepact.supernova.Main
import net.minepact.supernova.api.commands.Command
import net.minepact.supernova.api.commands.CommandResult
import net.minepact.supernova.api.commands.CommandUsage
import net.minepact.supernova.api.commands.arguments.CommandArguments
import net.minepact.supernova.api.permissions.Permission
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnCommand : Command(
    name = "spawn",
    description = "",
    permission = Permission("nexus.spawn.teleport"),
    aliases = listOf(""),
    playerOnly = true,
    usage = CommandUsage(label = "spawn",
        arguments = listOf()
    )
) {
    override fun execute(sender: CommandSender, args: CommandArguments): CommandResult {
        assert(sender is Player)
        val player = sender as Player

        player.teleport(Location(
            Bukkit.getWorld(Main.spawnData.world),
            Main.spawnData.x,
            Main.spawnData.y,
            Main.spawnData.z,
            Main.spawnData.yaw,
            Main.spawnData.pitch
        ))

        return CommandResult.SUCCESS
    }
}