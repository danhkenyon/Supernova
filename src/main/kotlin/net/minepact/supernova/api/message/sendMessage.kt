package net.minepact.supernova.api.message

import net.minepact.supernova.api.message.adapter.AdventureAdapter

fun org.bukkit.command.CommandSender.sendMessage(msg: Message) {
    sendMessage(AdventureAdapter.convert(msg))
}
fun org.bukkit.command.CommandSender.sendMessage(vararg messages: String) {
    val builder = MessageBuilder()
    for (message in messages) {
        builder.append(message)
    }
    sendMessage(AdventureAdapter.convert(builder.build()))
}