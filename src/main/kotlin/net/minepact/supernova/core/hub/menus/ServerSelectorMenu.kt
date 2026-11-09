package net.minepact.supernova.core.hub.menus

import net.minepact.supernova.api.items.ItemBuilder
import net.minepact.supernova.api.menus.*
import org.bukkit.Material

class ServerSelectorMenu : Menu() {
    override val id = "server_selector"
    override val title = "Select a server"
    override val rows = 3

    override fun build(
        context: MenuContext,
        layout: MenuLayout
    ) {
        val player = context.player

        layout.set(
            slot = 13,
            item = ItemBuilder(Material.SHULKER_BOX)
                .id("box_button")
                .name("<g:#f59f37:#b437f5><bold>Box PvP</bold></g>")
                .build(),
            action = {
                player.performCommand("server box")
            }
        )
    }
}