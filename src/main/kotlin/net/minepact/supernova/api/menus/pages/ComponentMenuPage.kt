package net.minepact.supernova.api.menus.pages

import net.minepact.supernova.api.menus.MenuSession
import net.minepact.supernova.api.menus.component.MenuComponent

class ComponentMenuPage(
    private val components: List<MenuComponent>
) : MenuPage {
    override fun render(session: MenuSession) {
        components.forEach(session::set)
    }
}