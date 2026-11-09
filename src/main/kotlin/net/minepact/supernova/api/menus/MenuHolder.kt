package net.minepact.supernova.api.menus

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class MenuHolder(
    val context: MenuContext,
    val layout: MenuLayout
) : InventoryHolder {
    private lateinit var menuInventory: Inventory

    fun attach(inventory: Inventory) {
        menuInventory = inventory
    }

    override fun getInventory(): Inventory = menuInventory
}