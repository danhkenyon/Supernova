package net.minepact.supernova.api.events.items

import net.minepact.supernova.api.items.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemDropEvent(
    player: Player,
    item: Item,
    itemStack: ItemStack,
    isCancelled: Boolean = false
) : CancellableItemEvent(player, item, itemStack, isCancelled)
