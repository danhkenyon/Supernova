package net.minepact.supernova.api.message.render

import net.minepact.supernova.api.message.Message

class PlainTextRenderer : MessageRenderer<String> {

    override fun render(message: Message): String {
        return message.plainText()
    }
}