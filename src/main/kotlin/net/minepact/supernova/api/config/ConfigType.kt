package net.minepact.supernova.api.config

import net.minepact.supernova.api.config.parser.ConfigParser
import net.minepact.supernova.api.config.parser.JsonParser
import net.minepact.supernova.api.config.parser.NexusParser
import net.minepact.supernova.api.config.parser.YamlParser
import kotlin.reflect.KClass

enum class ConfigType(val parser: KClass<out ConfigParser>) {
    YAML(YamlParser::class),
    JSON(JsonParser::class),
    NEXUS(NexusParser::class);
}