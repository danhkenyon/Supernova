package net.minepact.supernova.api.minecraft

import net.minepact.supernova.Main
import org.bukkit.NamespacedKey

object NamespacedKeys {
    private val keys: MutableMap<String, NamespacedKey> = mutableMapOf()

    fun get(key: String): NamespacedKey = keys.computeIfAbsent(key) { NamespacedKey(Main.instance, key) }
}