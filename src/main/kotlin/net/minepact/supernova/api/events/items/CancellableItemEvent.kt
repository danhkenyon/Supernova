package net.minepact.supernova.api.events.items

import net.minepact.supernova.api.events.Cancellable
import net.minepact.supernova.api.items.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class CancellableItemEvent(
    player: Player,
    item: Item,
    itemStack: ItemStack,
    override var isCancelled: Boolean = false
) : ItemEvent(player, item, itemStack), Cancellable