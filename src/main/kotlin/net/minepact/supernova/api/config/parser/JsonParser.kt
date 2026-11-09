package net.minepact.supernova.api.config.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import net.minepact.supernova.api.config.ConfigFile
import net.minepact.supernova.api.config.ConfigValue
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader
import java.io.FileWriter

class JsonParser : ConfigParser {
    private val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    override fun parse(configFile: ConfigFile): List<ConfigValue<Any>> {
        val file = configFile.getFile()

        val json = if (file.length() == 0L) JSONObject().also { FileWriter(file).use { it.write("{}") } }
        else FileReader(file).use { JSONParser().parse(it) as JSONObject }

        fun flatten(
            obj: JSONObject,
            prefix: String = ""
        ): List<ConfigValue<Any>> {
            return obj.entries.flatMap { entry ->
                val (key, value) = entry as Map.Entry<*, *>
                val fullKey = if (prefix.isEmpty()) key.toString() else "$prefix.$key"

                when (value) {
                    is JSONObject -> flatten(value, fullKey)
                    else -> listOf(ConfigValue(fullKey, value as Any))
                }
            }
        }

        return configFile.getConfigValues() + flatten(json)
    }

    override fun write(configFile: ConfigFile) {
        val json = JSONObject()

        configFile.getConfigValues().forEach { configValue ->
            val keys = configValue.key.split(".")
            var current = json

            keys.dropLast(1).forEach { current = current.getOrPut(it) { JSONObject() } as JSONObject }
            current[keys.last()] = configValue.value
        }

        FileWriter(configFile.getFile()).use {
            mapper.writeValue(it, json)
        }
    }
}