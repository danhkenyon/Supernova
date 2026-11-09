package net.minepact.supernova.api.menus

import net.minepact.supernova.api.menus.event.MenuClickEvent
import net.minepact.supernova.api.menus.event.MenuDragEvent
import net.minepact.supernova.api.message.Message

abstract class Menu(
    val title: Message,
    val size: Int
) {
    init {
        require(size in 9..54 && size % 9 == 0) { "Menu size must be between 9 and 54 and divisible by 9." }
    }

    abstract fun render(session: MenuSession)

    open fun onOpen(session: MenuSession) {}
    open fun onClose(session: MenuSession) {}

    open fun onClick(event: MenuClickEvent) {}
    open fun onDrag(event: MenuDragEvent) {}
}