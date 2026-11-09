package net.minepact.supernova

import net.minepact.supernova.api.commands.*
import net.minepact.supernova.api.config.*
import net.minepact.supernova.api.events.*
import net.minepact.supernova.api.items.*
import net.minepact.supernova.api.logger.Logger
import net.minepact.supernova.api.menus.MenuManager
import net.minepact.supernova.api.message.MessageSystem
import net.minepact.supernova.api.server.*
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
            private set
        lateinit var commandRegister: CommandRegister
            private set
        lateinit var eventManager: EventManager
            private set
        lateinit var serverInfo: ServerInfo
            private set
    }

    override fun onLoad() {
        instance = this
        commandRegister = CommandRegister()
        eventManager = EventManager()

        Logger.log("Loading config...")
        ConfigRegistry.create("config", "config.yml", ConfigType.YAML)
        val config: ConfigFile = ConfigRegistry.get("config")
            ?: throw IllegalStateException("Failed to create the config file.")

        Logger.log("Fetching server information...")
        val serverName = (config.setValueIfNull(
            ConfigValue("server.name", DEFAULT_SERVER_NAME)
        ).value) as String
        val serverTypeIdentifier = (config.setValueIfNull(
            ConfigValue("server.type", DEFAULT_SERVER_TYPE.name)
        ).value) as String
        serverInfo = ServerInfo(name = serverName, type = ServerType.fromName(serverTypeIdentifier))
    }

    override fun onEnable() {
        MessageSystem.initialize()

        MenuManager.initialize()

        Logger.log("Registering commands...")
        findCommands("net.minepact.supernova").forEach { commandRegister.register(it) }

        Logger.log("Registering events...")
        findListeners("net.minepact.supernova").forEach { eventManager.register(it) }

        Logger.log("Registering events...")
        findItems("net.minepact.supernova").forEach { ItemRegister.register(it) }
    }

    override fun onDisable() {
        Logger.log("Unloading config...")
        ConfigRegistry.all().forEach { (_, file) -> file.unload() }

        Logger.log("Unregistering commands...")
        commandRegister.unregisterAll()

        Logger.log("Unregistering events...")
        eventManager.unregisterAll()
    }
}
