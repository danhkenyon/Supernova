package net.minepact.supernova.api.message.helpers

import net.minepact.supernova.api.message.MessageBuilder

object Messages {
    val ARROW: MessageBuilder
        get() = MessageBuilder().append("<reset> <dark_gray>»</dark_gray> <reset>").copy()
    val ERROR_PREFIX: MessageBuilder
        get() = MessageBuilder().append("<hex:#d83e4a><bold>Error<reset>").append(ARROW.build()).copy()
    val NEXUS_PREFIX: MessageBuilder
        get() = MessageBuilder().append("<g:#6f0fc9:#a25ae4><bold>Nexus<reset>").append(ARROW.build())
}