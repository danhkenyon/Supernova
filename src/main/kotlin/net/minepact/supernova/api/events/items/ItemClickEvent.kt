package net.minepact.supernova.api.events.items

import net.minepact.supernova.api.items.Item
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

open class ItemClickEvent(
    player: Player,
    item: Item,
    itemStack: ItemStack,
    val action: Action,
    val hand: EquipmentSlot,
    isCancelled: Boolean = false
) : CancellableItemEvent(player, item, itemStack, isCancelled)

