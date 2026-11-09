package net.minepact.supernova.core.hub.events

import net.kyori.adventure.text.Component
import net.minepact.supernova.Main
import net.minepact.supernova.api.events.EventListener
import net.minepact.supernova.api.events.Subscribe
import net.minepact.supernova.api.items.ItemRegister
import net.minepact.supernova.api.message.MessageBuilder
import net.minepact.supernova.api.message.sendMessage
import net.minepact.supernova.api.server.ServerType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener : EventListener {

    @Subscribe(server = ServerType.HUB)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val inventory = player.inventory

        event.joinMessage(Component.text(""))

        player.teleport(Location(
            Bukkit.getWorld(Main.spawnData.world),
            Main.spawnData.x,
            Main.spawnData.y,
            Main.spawnData.z,
            Main.spawnData.yaw,
            Main.spawnData.pitch
        ))

        for (player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(MessageBuilder().append("<dark_gray>[<green><bold>+</bold></green>] <gray> ${player.name}").build())
        }

        inventory.clear()
        inventory.setItem(4, ItemRegister.get("server_selector")!!.asItemStack())
    }
}