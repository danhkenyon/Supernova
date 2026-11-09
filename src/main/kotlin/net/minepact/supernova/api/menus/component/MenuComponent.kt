package net.minepact.supernova.api.menus.component

import net.minepact.supernova.api.menus.MenuSession
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

interface MenuComponent {
    val slot: Int

    fun render(session: MenuSession): ItemStack
    fun onClick(session: MenuSession, event: InventoryClickEvent) {}
}