package net.minepact.supernova.core.hub.events

import net.minepact.supernova.api.events.EventListener
import net.minepact.supernova.api.events.Subscribe
import net.minepact.supernova.api.server.ServerType
import org.bukkit.Material
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack

class PlayerJoinListener : EventListener {

    @Subscribe(server = ServerType.HUB)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val inventory = player.inventory

        ItemStack.of(Material.COMPASS, 1)
        inventory.setItem(4, ItemStack.of(Material.COMPASS, 1))
    }
}