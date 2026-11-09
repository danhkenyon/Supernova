package net.minepact.supernova.api.message.render

import net.minepact.supernova.api.message.Message
import net.minepact.supernova.api.message.MessageTarget

inline fun <reified T : Any> Message.render(target: MessageTarget): T {
    return MessageRenderers.render(target, this)
}