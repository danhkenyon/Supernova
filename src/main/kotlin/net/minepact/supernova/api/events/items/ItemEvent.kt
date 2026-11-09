package net.minepact.supernova.api.events.items

import net.minepact.supernova.api.events.NexusEvent
import net.minepact.supernova.api.items.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class ItemEvent(
    val player: Player,
    val item: Item,
    val itemStack: ItemStack
) : NexusEvent()

