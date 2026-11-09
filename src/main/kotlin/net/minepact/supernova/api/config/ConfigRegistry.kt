package net.minepact.supernova.api.config

object ConfigRegistry {
    private val configs: MutableMap<String, ConfigFile> = mutableMapOf()

    fun all(): Map<String, ConfigFile> = configs

    fun create(
        key: String,
        path: String,
        type: ConfigType = ConfigType.NEXUS
    ): ConfigFile {
        val config = ConfigFile(key, "plugins/Supernova/${path}", type)

        config.load()
        return config
    }

    fun register(key: String, file: ConfigFile) {
        configs[key] = file
    }

    fun unregister(key: String) {
        if (configs.containsKey(key)) configs.remove(key)
    }

    fun get(key: String): ConfigFile? {
        configs.forEach { (name, file) -> if (name == key) return file }
        return null
    }
}