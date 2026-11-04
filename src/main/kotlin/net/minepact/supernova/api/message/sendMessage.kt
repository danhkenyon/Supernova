package net.minepact.supernova.api.message

import net.minepact.supernova.api.message.adapter.AdventureAdapter

fun org.bukkit.command.CommandSender.sendMessage(msg: Message) {
    sendMessage(AdventureAdapter.convert(msg))
}