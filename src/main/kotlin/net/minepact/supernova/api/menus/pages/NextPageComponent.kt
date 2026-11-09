package net.minepact.supernova.api.menus.pages

import net.minepact.supernova.api.menus.*
import net.minepact.supernova.api.menus.component.*
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@Suppress("DEPRECATION")
class NextPageComponent(
    override val slot: Int
) : MenuComponent {

    override fun render(session: MenuSession): ItemStack {
        val pagination = session.pagination

        val item = ItemStack(
            if (pagination?.hasNext == true) Material.ARROW
            else Material.GRAY_DYE
        )

        val meta = item.itemMeta

        meta?.setDisplayName(
            if (pagination?.hasNext == true)
                "§aNext Page"
            else
                "§7No Next Page"
        )

        item.itemMeta = meta

        return item
    }

    override fun onClick(
        session: MenuSession,
        event: InventoryClickEvent
    ) {
        session.pagination?.next()
    }
}