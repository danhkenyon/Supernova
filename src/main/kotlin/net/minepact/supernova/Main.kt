package net.minepact.supernova

import net.minepact.supernova.api.commands.CommandRegister
import net.minepact.supernova.api.commands.findCommands
import net.minepact.supernova.api.logger.Logger
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
            private set
        lateinit var commandRegister: CommandRegister
    }

    override fun onLoad() {
        instance = this
        commandRegister = CommandRegister()

        Logger.log("Loading Supernova...")

        findCommands("net.minepact.supernova.core").forEach { commandRegister.register(it) }
    }
    override fun onEnable() { }
    override fun onDisable() { }
}
