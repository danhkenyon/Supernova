package net.minepact.supernova.api.message.render

import net.kyori.adventure.text.Component
import net.minepact.supernova.api.message.Message
import net.minepact.supernova.api.message.adapter.AdventureAdapter

class AdventureMessageRenderer : MessageRenderer<Component> {

    override fun render(message: Message): Component {
        return AdventureAdapter.convert(message)
    }
}