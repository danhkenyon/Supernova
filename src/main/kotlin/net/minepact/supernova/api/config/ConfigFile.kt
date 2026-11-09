package net.minepact.supernova.api.config

import net.minepact.supernova.api.config.parser.ConfigParser
import java.io.File

open class ConfigFile(
    val name: String,
    path: String,
    configType: ConfigType
) {
    private val configFile: File = File(path)
    private val configValues: MutableList<ConfigValue<Any>> = mutableListOf()
    private val parser: ConfigParser

    private var isLoaded: Boolean = false

    init {
        configFile.parentFile?.mkdirs()
        if (!configFile.exists()) {
            configFile.createNewFile()
        }

        parser = configType.parser.java.getConstructor().newInstance()
        load()
    }

    fun getFile(): File = configFile
    @Synchronized
    fun getConfigValues(): List<ConfigValue<Any>> = configValues.toList()

    @Synchronized
    fun getValue(key: String): ConfigValue<Any>? = configValues.firstOrNull { it.key == key }
    @Synchronized
    fun setValue(newValue: ConfigValue<Any>): ConfigValue<Any> {
        configValues.removeIf { it.key == newValue.key }
        configValues.add(newValue)

        save()
        return newValue
    }

    @Synchronized
    fun removeValue(key: String) {
        if (configValues.removeIf { it.key == key }) save()
    }

    @Synchronized
    fun setValueIfNull(newValue: ConfigValue<Any>): ConfigValue<Any> = getValue(newValue.key) ?: setValue(newValue)

    @Synchronized
    fun load() {
        val parsedValues = parser.parse(this)
        configValues.clear()
        configValues.addAll(parsedValues)

        ConfigRegistry.register(name, this)
        isLoaded = true
    }

    @Synchronized
    fun unload() {
        if (!isLoaded) return

        save()
        ConfigRegistry.unregister(name)

        isLoaded = false
    }

    @Synchronized
    fun reload() {
        load()
    }

    @Synchronized
    fun save() {
        parser.write(this)
    }
}