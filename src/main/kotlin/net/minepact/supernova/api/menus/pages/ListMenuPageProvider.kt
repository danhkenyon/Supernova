package net.minepact.supernova.api.menus.pages

import net.minepact.supernova.api.menus.MenuSession

class ListMenuPageProvider(
    private val pages: List<MenuPage>
) : MenuPageProvider {
    override fun pageCount(session: MenuSession): Int {
        return pages.size
    }
    override fun getPage(session: MenuSession, index: Int): MenuPage? {
        return pages.getOrNull(index)
    }
}