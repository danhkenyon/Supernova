package net.minepact.supernova.api.menus.pages

import net.minepact.supernova.api.menus.MenuSession

interface MenuPageProvider {
    fun pageCount(session: MenuSession): Int
    fun getPage(session: MenuSession, index: Int): MenuPage?
}