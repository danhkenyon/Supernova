package net.minepact.supernova.core.hub.items

import net.minepact.supernova.api.items.Item
import net.minepact.supernova.api.items.ItemCallbacks
import net.minepact.supernova.api.menus.MenuManager
import org.bukkit.Material

class ServerSelectorItem : Item(
    id = "server_selector",
    material = Material.NETHER_STAR,
    name = "<red><bold>Server Selector</bold></red>",
    lore = listOf(
        " ",
        "<gray><italic>Use this server selector to travel through",
        "<gray><italic>the multiple servers on the network."
    ),
    amount = 1,
    customModelData = 1,
    unbreakable = true,
    glow = true,
    stackable = false,
    callbacks = ItemCallbacks(
        onDrop = { event ->
            event.isCancelled = true
            MenuManager.open(event.player, MenuManager.get("server_selector")!!)
        },
        onClick = { event ->
            event.isCancelled = true
            MenuManager.open(event.player, MenuManager.get("server_selector")!!)
        },
        onConsume = { event ->
            event.isCancelled = true
            MenuManager.open(event.player, MenuManager.get("server_selector")!!)
        },
        onLeftClick = { event ->
            event.isCancelled = true
            MenuManager.open(event.player, MenuManager.get("server_selector")!!)
        },
        onRightClick = { event ->
            event.isCancelled = true
            MenuManager.open(event.player, MenuManager.get("server_selector")!!)
        },
    )
)