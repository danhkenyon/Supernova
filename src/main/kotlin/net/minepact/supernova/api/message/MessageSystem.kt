package net.minepact.supernova.api.message

import net.minepact.supernova.api.message.render.*

object MessageSystem {
    private var initialized = false

    fun initialize() {
        if (initialized) return

        MessageRenderers.register(MessageTarget.CHAT, AdventureMessageRenderer())
        MessageRenderers.register(MessageTarget.ITEM_NAME, AdventureMessageRenderer())
        MessageRenderers.register(MessageTarget.ITEM_LORE, AdventureMessageRenderer())
        MessageRenderers.register(MessageTarget.MENU_TITLE, AdventureMessageRenderer())
        MessageRenderers.register(MessageTarget.ACTION_BAR, AdventureMessageRenderer())
        MessageRenderers.register(MessageTarget.TITLE, AdventureMessageRenderer())
        MessageRenderers.register(MessageTarget.SUBTITLE, AdventureMessageRenderer())
        MessageRenderers.register(MessageTarget.CONSOLE, PlainTextRenderer())

        initialized = true
    }
}