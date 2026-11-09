package net.minepact.supernova.api.menus

import org.bukkit.entity.Player

@Suppress("UNCHECKED_CAST")
class MenuContext internal constructor(
    val player: Player,
    val menu: Menu
) {
    private val values = mutableMapOf<String, Any>()

    var currentPage: Int = 0
        internal set
    var isOpen: Boolean = false
        internal set

    fun <T : Any> set(key: String, value: T) {
        values[key] = value
    }
    fun <T : Any> get(key: String): T? {
        return values[key] as? T
    }

    fun remove(key: String) {
        values.remove(key)
    }
    fun clear() {
        values.clear()
    }
}