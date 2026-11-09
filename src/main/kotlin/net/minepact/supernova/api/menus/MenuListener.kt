package net.minepact.supernova.api.menus

import net.minepact.supernova.api.events.EventListener
import net.minepact.supernova.api.events.Subscribe
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

internal class MenuListener : EventListener {

    @Subscribe
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? org.bukkit.entity.Player ?: return
        val session = MenuManager.session(player) ?: return

        if (event.view.topInventory !== session.inventory) return

        event.isCancelled = true

        val slot = event.rawSlot
        if (slot !in 0 until session.menu.size) return

        session.menu.onClick(session, event)
        if (MenuManager.session(player) !== session) return

        session.component(slot)?.onClick(session, event)
    }

    @Subscribe
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? org.bukkit.entity.Player ?: return
        val session = MenuManager.session(player) ?: return

        if (event.view.topInventory !== session.inventory) return

        val touchesMenu = event.rawSlots.any { it in 0 until session.menu.size }
        if (!touchesMenu) return

        event.isCancelled = true
        session.menu.onDrag(session, event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? org.bukkit.entity.Player ?: return
        val session = MenuManager.session(player) ?: return

        if (event.inventory !== session.inventory) return

        MenuManager.removeSession(session)
    }
}