package net.minepact.supernova.api.items

import net.minepact.supernova.api.message.MessageBuilder
import net.minepact.supernova.api.message.MessageTarget
import net.minepact.supernova.api.message.render.MessageRenderers
import net.minepact.supernova.api.minecraft.NamespacedKeys
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

open class Item internal constructor(
    val id: String,
    val material: Material,
    val name: String?,
    val lore: List<String>,
    val amount: Int,
    val customModelData: Int?,
    val unbreakable: Boolean,
    val glow: Boolean,
    val stackable: Boolean,
    val callbacks: ItemCallbacks
) : Cloneable {
    internal fun asItemStack(): ItemStack {
        val stack = ItemStack(this.material, this.amount)
        val meta = stack.itemMeta

        this.name?.let {
            meta.displayName(MessageRenderers.render(
                MessageTarget.ITEM_NAME,
                MessageBuilder().append(it).build()
            ))
        }
        if (this.lore.isNotEmpty()) {
            meta.lore(this.lore.map { MessageRenderers.render(
                MessageTarget.ITEM_LORE,
                MessageBuilder().append(it).build()
            ) })
        }

        meta.isUnbreakable = this.unbreakable
        @Suppress("DEPRECATION")
        this.customModelData?.let { meta.setCustomModelData(it) }

        if (this.glow) {
            meta.addEnchant(Enchantment.LURE, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        if (!this.stackable) {
            meta.persistentDataContainer.set(NamespacedKeys.get("uuid"), PersistentDataType.STRING, UUID.randomUUID().toString())
        }

        meta.persistentDataContainer.set(NamespacedKeys.get("item_id"), PersistentDataType.STRING, this.id)
        stack.itemMeta = meta
        return stack
    }

    public override fun clone(): Item {
        return super.clone() as Item
    }
}

