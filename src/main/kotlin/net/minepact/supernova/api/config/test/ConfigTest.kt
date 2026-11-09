package net.minepact.supernova.api.config.test

import net.minepact.supernova.api.config.ConfigFile
import net.minepact.supernova.api.config.ConfigRegistry
import net.minepact.supernova.api.config.ConfigValue
import net.minepact.supernova.api.logger.LogType
import net.minepact.supernova.api.logger.Logger

class ConfigTest {
    fun test() {
        val config: ConfigFile = ConfigRegistry.get("config")
            ?: throw IllegalStateException("An error occurred. Failed to correctly create and register the config.")

        config.setValue(ConfigValue("spawn.world", "world"))
        config.setValue(ConfigValue("spawn.x", 0.0))
        config.setValue(ConfigValue("spawn.y", 0.0))
        config.setValue(ConfigValue("spawn.z", 0.0))
        config.setValue(ConfigValue("spawn.yaw", 0.0))
        config.setValue(ConfigValue("spawn.pitch", 0.0))

        config.reload()

        Logger.log(
            "Config Test: spawn[world: ${
                config.getValue("spawn.world")
            }, x: ${
                config.getValue("spawn.x")
            }, y: ${
                config.getValue("spawn.y")
            }, z: ${
                config.getValue("spawn.z")
            }, yaw: ${
                config.getValue("spawn.yaw")
            }, pitch: ${
                config.getValue("spawn.pitch")
            }]", LogType.DEBUG
        )

        config.reload()

        config.removeValue("spawn.x")
        config.removeValue("spawn.yaw")

        config.reload()

        Logger.log("New X: ${config.getValue("spawn.x")}, New Yaw: ${config.getValue("spawn.yaw")}")
    }
}