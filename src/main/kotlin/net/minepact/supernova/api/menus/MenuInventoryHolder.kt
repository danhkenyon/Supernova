package net.minepact.supernova.api.menus

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class MenuInventoryHolder(val session: MenuSession) : InventoryHolder {
    private lateinit var menuInventory: Inventory

    fun setInventory(inventory: Inventory) {
        menuInventory = inventory
    }

    override fun getInventory(): Inventory {
        return menuInventory
    }
}