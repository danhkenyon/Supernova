package net.minepact.supernova

import net.minepact.supernova.api.commands.CommandRegister
import net.minepact.supernova.api.commands.findCommands
import net.minepact.supernova.api.config.ConfigFile
import net.minepact.supernova.api.config.ConfigRegistry
import net.minepact.supernova.api.config.ConfigType
import net.minepact.supernova.api.config.ConfigValue
import net.minepact.supernova.api.events.EventManager
import net.minepact.supernova.api.events.findListeners
import net.minepact.supernova.api.logger.Logger
import net.minepact.supernova.api.menus.MenuManager
import net.minepact.supernova.api.server.DEFAULT_SERVER_NAME
import net.minepact.supernova.api.server.DEFAULT_SERVER_TYPE
import net.minepact.supernova.api.server.ServerInfo
import net.minepact.supernova.api.server.ServerType
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        private const val CORE_PACKAGE: String = "net.minepact.supernova.core"

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
        val config: ConfigFile = ConfigRegistry.get("config") ?: throw IllegalStateException("Failed to create the config file.")

        Logger.log("Fetching server information...")

        val serverName: String = (config.setValueIfNull(ConfigValue("server.name", DEFAULT_SERVER_NAME)).value) as String
        val serverTypeIdentifier: String = (config.setValueIfNull(ConfigValue("server.type", DEFAULT_SERVER_TYPE.name)).value) as String
        val serverType: ServerType = ServerType.fromName(serverTypeIdentifier)

        serverInfo = ServerInfo(name = serverName, type = serverType)
    }

    override fun onEnable() {
        MenuManager.initialize()

        Logger.log("Registering commands...")
        findCommands("net.minepact.supernova").forEach { commandRegister.register(it) }
        Logger.log("Registering events...")
        findListeners("net.minepact.supernova").forEach { eventManager.register(it) }
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
