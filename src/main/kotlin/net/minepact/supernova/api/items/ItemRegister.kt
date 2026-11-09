package net.minepact.supernova.api.items

import net.minepact.supernova.api.minecraft.NamespacedKeys
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemRegister {
    private val items = linkedMapOf<String, Item>()

    fun register(item: Item, replace: Boolean = false): Item {
        require(replace || item.id !in items) { "Item '${item.id}' is already registered." }

        items[item.id] = item
        return item
    }
    fun unregister(id: String): Item? = items.remove(id)

    fun get(id: String): Item? = items[id]
    fun get(stack: ItemStack?): Item? {
        if (stack == null || stack.type.isAir || !stack.hasItemMeta()) return null

        val id = stack.itemMeta.persistentDataContainer.get(
            NamespacedKeys.get("item_id"),
            PersistentDataType.STRING
        ) ?: return null

        return items[id]
    }

    fun all(): List<Item> = items.values.toList()

    fun clear() {
        items.clear()
    }

    fun create(id: String): ItemStack? {
        val item = get(id) ?: return null
        return item.asItemStack()
    }
}