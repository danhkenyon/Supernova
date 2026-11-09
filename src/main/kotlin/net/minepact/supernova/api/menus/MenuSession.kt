package net.minepact.supernova.api.menus

import net.minepact.supernova.api.menus.component.MenuComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class MenuSession(
    val player: Player,
    val menu: Menu
) {
    lateinit var inventory: Inventory
        private set

    var isOpen: Boolean = false
        private set

    private val components: MutableMap<Int, MenuComponent> = mutableMapOf()

    fun setComponent(slot: Int, component: MenuComponent) {
        require(slot in 0 until menu.size)

        components[slot] = component
        inventory.setItem(slot, component.item)
    }

    fun removeComponent(slot: Int) {
        components.remove(slot)
        inventory.setItem(slot, null)
    }

    fun component(slot: Int): MenuComponent? =
        components[slot]

    fun refresh() {
        menu.render(this)
    }

    internal fun createInventory() {
        inventory = Bukkit.createInventory(
            MenuInventoryHolder(this),
            menu.size,
            menu.title
        )
    }
}