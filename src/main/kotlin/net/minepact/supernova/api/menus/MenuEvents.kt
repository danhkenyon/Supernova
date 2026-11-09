package net.minepact.supernova.api.menus

import net.minepact.supernova.api.events.EventListener
import net.minepact.supernova.api.events.Subscribe
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class MenuEvents() : EventListener {
    @Subscribe
    fun onClick(event: InventoryClickEvent) {
        val holder = event.view.topInventory.holder as? MenuHolder ?: return
        val context = holder.context

        if (!context.isOpen) return

        val topInventory = event.view.topInventory
        val clickedInventory = event.clickedInventory

        if (clickedInventory == null) {
            event.isCancelled = true
            return
        }

        if (clickedInventory == topInventory) {
            val slot = event.rawSlot

            event.isCancelled = true

            if (slot !in 0 until topInventory.size) return

            context.menu.onClick(context, slot)

            val holder = topInventory.holder as? MenuHolder ?: return
            val slotData = holder.layout.get(slot) ?: return

            slotData.action?.invoke(Bukkit.getPlayer(event.whoClicked.name)!!)
            event.isCancelled = true
        }
    }

    @Subscribe
    fun onDrag(event: InventoryDragEvent) {
        val holder = event.view.topInventory.holder as? MenuHolder ?: return
        val context = holder.context

        if (!context.isOpen) return
        val topSize = event.view.topInventory.size

        val affectedSlots = event.rawSlots.filter { it < topSize }.toSet()
        if (affectedSlots.isNotEmpty()) {
            event.isCancelled = true
            context.menu.onDrag(context, affectedSlots)
        }
    }

    @Subscribe
    fun onClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder as? MenuHolder ?: return
        val context = holder.context

        if (context.isOpen) MenuManager.close(context.player)
    }
}