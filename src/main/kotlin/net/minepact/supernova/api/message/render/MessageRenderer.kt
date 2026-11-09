package net.minepact.supernova.api.message.render

import net.minepact.supernova.api.message.Message

interface MessageRenderer<T> {
    fun render(message: Message): T
}