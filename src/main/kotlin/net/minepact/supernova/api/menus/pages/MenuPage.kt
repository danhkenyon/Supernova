package net.minepact.supernova.api.menus.pages

import net.minepact.supernova.api.menus.MenuSession

interface MenuPage {
    fun render(session: MenuSession)

    fun onOpen(session: MenuSession) {}
    fun onClose(session: MenuSession) {}
}