package net.minepact.supernova.api.commands.arguments

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import java.nio.file.Files
import java.nio.file.Paths

object Provider {

    val EMPTY: (CommandSender) -> List<String> = {
        emptyList()
    }

    val PLAYERS: (CommandSender) -> List<String> = {
        Bukkit.getOnlinePlayers().map { it.name }
    }

    val WORLDS: (CommandSender) -> List<String> = { sender ->
        sender.server.worlds.map { it.name }
    }

    val GAME_MODES: (CommandSender) -> List<String> = {
        GameMode.entries.map { it.name.lowercase() }
    }

    val CONFIG_FILES: (CommandSender) -> List<String> = {
        val root = Paths.get("plugins", "Supernova")

        if (!Files.exists(root)) emptyList()
        else {
            Files.walk(root).use { files ->
                files
                    .filter { Files.isRegularFile(it) }
                    .filter {
                        val name = it.fileName.toString().lowercase()
                        name.endsWith(".json") ||
                                name.endsWith(".yml") ||
                                name.endsWith(".nexus")
                    }
                    .map {
                        root.relativize(it)
                            .toString()
                            .replace("\\", "/")
                    }
                    .toList()
            }
        }
    }
}