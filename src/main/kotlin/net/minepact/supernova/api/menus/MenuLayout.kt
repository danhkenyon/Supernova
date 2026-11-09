package net.minepact.supernova.api.menus

import net.minepact.supernova.api.items.Item
import org.bukkit.entity.Player

class MenuLayout(
    val rows: Int
) {
    private val contents = mutableMapOf<Int, Slot>()

    data class Slot(
        val item: Item,
        val action: ((Player) -> Unit)? = null,
        val movable: Boolean = false
    )

    fun set(
        slot: Int,
        item: Item,
        movable: Boolean = false,
        action: ((Player) -> Unit)? = null
    ) {
        require(slot in 0 until rows * 9) { "Slot $slot is outside the menu" }

        contents[slot] = Slot(
            item = item,
            action = action,
            movable = movable
        )
    }
    fun fill(item: Item) {
        for (slot in 0 until rows * 9) {
            contents[slot] = Slot(item.clone())
        }
    }

    fun remove(slot: Int) {
        contents.remove(slot)
    }
    fun clear() {
        contents.clear()
    }

    internal fun get(slot: Int): Slot? = contents[slot]
    internal fun entries(): Map<Int, Slot> = contents.toMap()
}