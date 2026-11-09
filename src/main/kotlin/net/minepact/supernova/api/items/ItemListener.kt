package net.minepact.supernova.api.items

import net.minepact.supernova.Main
import net.minepact.supernova.api.events.EventListener
import net.minepact.supernova.api.events.Subscribe
import net.minepact.supernova.api.events.items.ItemConsumeEvent
import net.minepact.supernova.api.events.items.ItemDropEvent
import net.minepact.supernova.api.events.items.ItemLeftClickEvent
import net.minepact.supernova.api.events.items.ItemRightClickEvent
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.EquipmentSlot

@Suppress("DEPRECATION")
class ItemListener : EventListener {
    @Subscribe(ignoreCancelled = false)
    fun onInteract(event: PlayerInteractEvent) {
        val action = event.action
        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK &&
            action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK
        ) return

        val stack = event.item ?: return
        val item = ItemRegister.get(stack) ?: return
        val hand = event.hand ?: EquipmentSlot.HAND

        val customEvent = if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            ItemLeftClickEvent(event.player, item, stack, action, hand, event.isCancelled)
        } else {
            ItemRightClickEvent(event.player, item, stack, action, hand, event.isCancelled)
        }

        Main.eventManager.call(customEvent)
        item.callbacks.onClick?.invoke(customEvent)
        when (customEvent) {
            is ItemLeftClickEvent -> item.callbacks.onLeftClick?.invoke(customEvent)
            is ItemRightClickEvent -> item.callbacks.onRightClick?.invoke(customEvent)
        }
        if (customEvent.isCancelled) event.isCancelled = true
    }

    @Subscribe(ignoreCancelled = false)
    fun onDrop(event: PlayerDropItemEvent) {
        val stack = event.itemDrop.itemStack
        val item = ItemRegister.get(stack) ?: return
        val customEvent = ItemDropEvent(event.player, item, stack, event.isCancelled)
        Main.eventManager.call(customEvent)
        item.callbacks.onDrop?.invoke(customEvent)
        if (customEvent.isCancelled) event.isCancelled = true
    }

    @Subscribe(ignoreCancelled = false)
    fun onConsume(event: PlayerItemConsumeEvent) {
        val item = ItemRegister.get(event.item) ?: return
        val customEvent = ItemConsumeEvent(event.player, item, event.item, event.isCancelled)
        Main.eventManager.call(customEvent)
        item.callbacks.onConsume?.invoke(customEvent)
        if (customEvent.isCancelled) event.isCancelled = true
    }
}
