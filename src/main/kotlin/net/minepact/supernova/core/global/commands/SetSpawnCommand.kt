package net.minepact.supernova.core.global.commands

import net.minepact.supernova.Main
import net.minepact.supernova.api.commands.Command
import net.minepact.supernova.api.commands.CommandResult
import net.minepact.supernova.api.commands.CommandUsage
import net.minepact.supernova.api.commands.arguments.CommandArguments
import net.minepact.supernova.api.config.ConfigFile
import net.minepact.supernova.api.config.ConfigRegistry
import net.minepact.supernova.api.config.ConfigValue
import net.minepact.supernova.api.message.action.Action
import net.minepact.supernova.api.message.helpers.Messages.ERROR_PREFIX
import net.minepact.supernova.api.message.helpers.Messages.NEXUS_PREFIX
import net.minepact.supernova.api.message.sendMessage
import net.minepact.supernova.api.minecraft.spawn.SpawnData
import net.minepact.supernova.api.permissions.Permission
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetSpawnCommand : Command(
    name = "set-spawn",
    description = "",
    permission = Permission("nexus.spawn.set"),
    aliases = listOf(""),
    playerOnly = true,
    usage = CommandUsage(label = "spawn",
        arguments = listOf()
    )
) {
    override fun execute(sender: CommandSender, args: CommandArguments): CommandResult {
        assert(sender is Player)
        val player = sender as Player

        val config: ConfigFile? = ConfigRegistry.get("config")
        if (config == null) {
            sender.sendMessage(ERROR_PREFIX
                    .append("<red>The file you entered is not registered. If this is an error, contact a plugin developer.")
                    .build()
            )
            return CommandResult.FAILURE
        }

        config.setValue(ConfigValue("spawn.world", player.world.name))
        config.setValue(ConfigValue("spawn.x", player.x))
        config.setValue(ConfigValue("spawn.y", player.y))
        config.setValue(ConfigValue("spawn.z", player.z))
        config.setValue(ConfigValue("spawn.yaw", player.yaw))
        config.setValue(ConfigValue("spawn.pitch", player.pitch))

        Main.spawnData = SpawnData(
            world = player.world.name,
            x = player.location.x,
            y = player.location.y,
            z = player.location.z,
            yaw = player.yaw,
            pitch = player.pitch
        )

        sender.sendMessage(NEXUS_PREFIX
            .append("<green>Set spawn at your current location.") {
                action(Action.SHOW_TEXT,
                        "<yellow>World: <white>${player.world.name}\n"+
                        "<yellow>X: <white>${player.x}\n"+
                        "<yellow>Y: <white>${player.y}\n"+
                        "<yellow>Z: <white>${player.z}\n"+
                        "<yellow>Yaw: <white>${player.yaw}\n"+
                        "<yellow>Pitch: <white>${player.pitch}\n"
                )
            }
            .build()
        )

        return CommandResult.SUCCESS
    }
}