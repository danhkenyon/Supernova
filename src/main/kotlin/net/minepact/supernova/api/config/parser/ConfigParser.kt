package net.minepact.supernova.api.config.parser

import net.minepact.supernova.api.config.ConfigFile
import net.minepact.supernova.api.config.ConfigValue

interface ConfigParser {
    fun parse(configFile: ConfigFile): List<ConfigValue<Any>>
    fun write(configFile: ConfigFile)
}