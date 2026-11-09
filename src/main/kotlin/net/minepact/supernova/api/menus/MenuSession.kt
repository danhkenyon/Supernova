package net.minepact.supernova.api.menus

import net.minepact.supernova.api.menus.component.MenuComponent
import net.minepact.supernova.api.menus.pages.MenuPageProvider
import net.minepact.supernova.api.menus.pages.MenuPagination
import net.minepact.supernova.api.message.adapter.AdventureAdapter
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.UUID

class MenuSession(
    val player: Player,
    val menu: Menu
) {
    private val componentMap = LinkedHashMap<Int, MenuComponent>()
    private var inventoryInstance: Inventory? = null

    var pagination: MenuPagination? = null
        private set
    val playerId: UUID
        get() = player.uniqueId
    val inventory: Inventory
        get() = inventoryInstance ?: error("Menu inventory has not been created")
    val isOpen: Boolean
        get() = player.openInventory.topInventory === inventoryInstance
    val components: Map<Int, MenuComponent>
        get() = componentMap.toMap()

    internal fun createInventory(): Inventory {
        val holder = MenuInventoryHolder(this)

        val inventory = Bukkit.createInventory(
            holder,
            menu.size,
            AdventureAdapter.convert(menu.title)
        )

        holder.setInventory(inventory)
        inventoryInstance = inventory

        return inventory
    }

    fun set(component: MenuComponent) {
        require(component.slot in 0 until menu.size) { "Component slot ${component.slot} is outside the menu" }
        componentMap[component.slot] = component

        if (inventoryInstance != null) {
            inventory.setItem(
                component.slot,
                component.render(this)
            )
        }
    }
    fun remove(slot: Int) {
        componentMap.remove(slot)
        inventoryInstance?.setItem(slot, null)
    }
    fun component(slot: Int): MenuComponent? {
        return componentMap[slot]
    }

    fun paginate(provider: MenuPageProvider): MenuPagination {
        pagination?.current?.onClose(this)

        val controller = MenuPagination(this, provider)

        pagination = controller

        controller.first()

        return controller
    }

    fun clear() {
        componentMap.clear()
        inventoryInstance?.clear()
    }
    fun clearPagination() {
        pagination?.current?.onClose(this)
        pagination = null
    }
    fun refresh() {
        inventory.clear()
        val activePage = pagination?.current

        if (activePage != null) activePage.render(this)
        else menu.render(this)
    }
    fun refreshPage(render: () -> Unit) {
        inventory.clear()
        render()
    }

    internal fun display() {
        if (inventoryInstance == null) createInventory()

        refresh()
        player.openInventory(inventory)
    }
}