package net.minepact.supernova.api.config.parser

import net.minepact.supernova.api.config.ConfigFile
import net.minepact.supernova.api.config.ConfigValue
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.FileReader
import java.io.FileWriter

class YamlParser : ConfigParser {
    override fun parse(configFile: ConfigFile): List<ConfigValue<Any>> {
        val yaml = FileReader(configFile.getFile()).use {
            Yaml().load<Map<String, Any>>(it) ?: emptyMap()
        }

        fun flatten(
            map: Map<String, Any>,
            prefix: String = ""
        ): List<ConfigValue<Any>> {
            return map.flatMap { (key, value) ->
                val fullKey = if (prefix.isEmpty()) key
                else "$prefix.$key"

                when (value) {
                    is Map<*, *> -> flatten(
                        value.entries.associate { (k, v) -> k.toString() to v as Any },
                        fullKey
                    )

                    else -> listOf(ConfigValue(fullKey, value))
                }
            }
        }

        return flatten(yaml)
    }

    @Suppress("UNCHECKED_CAST")
    override fun write(configFile: ConfigFile) {
        val options = DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            isPrettyFlow = true
            indent = 2
            indicatorIndent = 0
        }

        val yaml = Yaml(options)
        val data = mutableMapOf<String, Any>()

        configFile.getConfigValues().forEach { configValue ->
            val keys = configValue.key.split(".")
            var current = data

            keys.dropLast(1).forEach { key ->
                current = current.getOrPut(key) {
                    mutableMapOf<String, Any>()
                } as MutableMap<String, Any>
            }

            current[keys.last()] = configValue.value
        }

        FileWriter(configFile.getFile()).use {
            yaml.dump(data, it)
        }
    }
}
