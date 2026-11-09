package net.minepact.supernova.api.menus.component

import net.minepact.supernova.api.menus.MenuSession
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class SimpleMenuComponent(
    override val slot: Int,
    private val item: (MenuSession) -> ItemStack,
    private val click: (MenuSession, InventoryClickEvent) -> Unit = { _, _ -> }
) : MenuComponent {
    override fun render(session: MenuSession): ItemStack {
        return item(session)
    }
    override fun onClick(session: MenuSession, event: InventoryClickEvent) {
        click(session, event)
    }
}