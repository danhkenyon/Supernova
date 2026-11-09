package net.minepact.supernova.api.menus

import net.minepact.supernova.api.message.Message
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

abstract class Menu(
    val title: Message,
    val size: Int
) {
    init {
        require(size in 9..54) { "Menu size must be between 9 and 54" }
        require(size % 9 == 0) { "Menu size must be divisible by 9" }
    }

    abstract fun render(session: MenuSession)

    open fun onOpen(session: MenuSession) {}
    open fun onClose(session: MenuSession) {}
    open fun onClick(session: MenuSession, event: InventoryClickEvent) {}
    open fun onDrag(session: MenuSession, event: InventoryDragEvent) {}

    fun open(player: Player): MenuSession {
        return MenuManager.open(player, this)
    }
}