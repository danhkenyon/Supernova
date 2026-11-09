package net.minepact.supernova.api.items

import net.minepact.supernova.api.events.items.ItemClickEvent
import net.minepact.supernova.api.events.items.ItemConsumeEvent
import net.minepact.supernova.api.events.items.ItemDropEvent
import net.minepact.supernova.api.events.items.ItemLeftClickEvent
import net.minepact.supernova.api.events.items.ItemRightClickEvent
import org.bukkit.Material

class ItemBuilder(private val material: Material) {
    private var id: String? = null
    private var name: String? = null
    private var lore: List<String> = emptyList()
    private var amount = 1
    private var customModelData: Int? = null
    private var unbreakable = false
    private var stackable = true
    private var glow = false
    private var onClick: ((ItemClickEvent) -> Unit)? = null
    private var onLeftClick: ((ItemLeftClickEvent) -> Unit)? = null
    private var onRightClick: ((ItemRightClickEvent) -> Unit)? = null
    private var onDrop: ((ItemDropEvent) -> Unit)? = null
    private var onConsume: ((ItemConsumeEvent) -> Unit)? = null

    fun id(id: String) = apply { this.id = id }
    fun name(name: String?) = apply { this.name = name }
    fun lore(vararg lines: String) = apply { lore = lines.toList() }
    fun lore(lines: List<String>) = apply { lore = lines.toList() }
    fun amount(amount: Int) = apply { this.amount = amount }
    fun customModelData(value: Int?) = apply { customModelData = value }
    fun unbreakable(value: Boolean = true) = apply { unbreakable = value }
    fun stackable(value: Boolean) = apply { stackable = value }
    fun glow(value: Boolean = true) = apply { glow = value }

    fun onClick(handler: (ItemClickEvent) -> Unit) = apply { onClick = handler }
    fun onLeftClick(handler: (ItemLeftClickEvent) -> Unit) = apply { onLeftClick = handler }
    fun onRightClick(handler: (ItemRightClickEvent) -> Unit) = apply { onRightClick = handler }
    fun onDrop(handler: (ItemDropEvent) -> Unit) = apply { onDrop = handler }
    fun onConsume(handler: (ItemConsumeEvent) -> Unit) = apply { onConsume = handler }

    fun build(): Item {
        val itemId = requireNotNull(id) { "A custom item must have an ID." }
        require(itemId.matches(Regex("[a-z0-9][a-z0-9_.-]*"))) { "Invalid custom item ID '$itemId'. Use lowercase letters, numbers, '.', '_' or '-'." }
        require(amount in 1..material.maxStackSize) { "Amount must be between 1 and ${material.maxStackSize} for $material." }
        require(customModelData == null || customModelData!! >= 0) { "Custom model data cannot be negative." }

        return Item(
            itemId, material, name, lore.toList(), amount, customModelData,
            unbreakable, stackable, glow, ItemCallbacks(onClick, onLeftClick, onRightClick, onDrop, onConsume)
        )
    }
}
