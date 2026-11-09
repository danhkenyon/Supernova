package net.minepact.supernova

import net.minepact.supernova.api.commands.*
import net.minepact.supernova.api.config.*
import net.minepact.supernova.api.events.*
import net.minepact.supernova.api.items.*
import net.minepact.supernova.api.logger.Logger
import net.minepact.supernova.api.menus.MenuManager
import net.minepact.supernova.api.menus.MenuRenderer
import net.minepact.supernova.api.menus.findMenus
import net.minepact.supernova.api.message.MessageSystem
import net.minepact.supernova.api.minecraft.spawn.SpawnData
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
        lateinit var spawnData: SpawnData
    }

    override fun onLoad() {
        instance = this
        commandRegister = CommandRegister()
        eventManager = EventManager()

        Logger.log("Loading config...")
        ConfigRegistry.create("config", "config.yml", ConfigType.YAML)
        val config: ConfigFile = ConfigRegistry.get("config") ?: throw IllegalArgumentException("The config file could not be loaded!")

        Logger.log("Fetching server information...")
        val serverName = (config.setValueIfNull(ConfigValue("server.name", DEFAULT_SERVER_NAME)).value) as String
        val serverTypeIdentifier = (config.setValueIfNull(ConfigValue("server.type", DEFAULT_SERVER_TYPE.name)).value) as String
        serverInfo = ServerInfo(name = serverName, type = ServerType.fromName(serverTypeIdentifier))

        Logger.log("Fetching spawn information...")
        spawnData = SpawnData(
            world = config.setValueIfNull(ConfigValue("spawn.world", "world")).value as String,
            x = config.setValueIfNull(ConfigValue("spawn.x", 0.0)).value as Double,
            y = config.setValueIfNull(ConfigValue("spawn.y", 0.0)).value as Double,
            z = config.setValueIfNull(ConfigValue("spawn.z", 0.0)).value as Double,
            yaw = (config.setValueIfNull(ConfigValue("spawn.yaw", 0.0)).value as Double).toFloat(),
            pitch = (config.setValueIfNull(ConfigValue("spawn.pitch", 0.0)).value as Double).toFloat(),
        )
    }

    override fun onEnable() {
        MessageSystem.initialize()

        Logger.log("Registering commands...")
        findCommands("net.minepact.supernova").forEach { commandRegister.register(it) }

        Logger.log("Registering menus...")
        findMenus("net.minepact.supernova").forEach { MenuManager.register(it) }

        Logger.log("Registering items...")
        findItems("net.minepact.supernova").forEach { ItemRegister.register(it) }

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

        MenuManager.shutdown()
    }
}
