package net.minepact.supernova.api.menus

import org.bukkit.entity.Player
import java.util.UUID

object MenuManager {
    private val menus = mutableMapOf<String, Menu>()
    private val active = mutableMapOf<UUID, MenuContext>()
    private val renderer = MenuRenderer()

    fun register(menu: Menu) {
        menu.validate()
        require(!menus.containsKey(menu.id)) { "Menu '${menu.id}' is already registered" }

        menus[menu.id] = menu
    }
    fun unregister(id: String) {
        menus.remove(id)
        active.values
            .filter { it.menu.id == id }
            .toList()
            .forEach { close(it.player) }
    }

    fun get(id: String): Menu? = menus[id]

    fun open(player: Player, id: String) {
        val menu = menus[id] ?: error("Menu '$id' is not registered")
        open(player, menu)
    }
    fun open(player: Player, menu: Menu) {
        menu.validate()
        close(player)

        val context = MenuContext(player, menu)
        active[player.uniqueId] = context

        val holder = renderer.render(context)
        player.openInventory(holder.inventory)

        context.isOpen = true
        menu.onOpen(context)
    }

    fun refresh(player: Player) {
        val context = active[player.uniqueId] ?: return
        if (!context.isOpen) return

        val holder = renderer.render(context)
        player.openInventory(holder.inventory)
    }
    fun close(player: Player) {
        val context = active.remove(player.uniqueId) ?: return

        context.isOpen = false

        context.menu.onClose(context)
        player.closeInventory()

        context.menu.onDestroy(context)
        context.clear()
    }
    fun shutdown() {
        active.values.toList().forEach { close(it.player) }
        menus.clear()
    }

    fun handleInventoryClose(player: Player, holder: MenuHolder) {
        val context = active[player.uniqueId] ?: return

        if (context !== holder.context) return

        active.remove(player.uniqueId)
        context.isOpen = false

        context.menu.onClose(context)
        context.menu.onDestroy(context)
        context.clear()
    }

    fun getContext(player: Player): MenuContext? = active[player.uniqueId]
    fun isInMenu(player: Player): Boolean = active.containsKey(player.uniqueId)
}