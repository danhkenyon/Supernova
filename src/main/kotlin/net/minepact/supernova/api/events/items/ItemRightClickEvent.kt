package net.minepact.supernova.api.events.items

import net.minepact.supernova.api.items.Item
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ItemRightClickEvent(
    player: Player,
    item: Item,
    itemStack: ItemStack,
    action: Action,
    hand: EquipmentSlot,
    isCancelled: Boolean = false
) : ItemClickEvent(player, item, itemStack, action, hand, isCancelled)