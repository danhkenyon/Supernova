package net.minepact.supernova.api.message

import net.minepact.supernova.api.message.action.MessageAction
import net.minepact.supernova.api.message.colour.MessageStyle

data class MessageSegment(
    val text: String,
    val style: MessageStyle = MessageStyle(),
    val actions: List<MessageAction> = emptyList()
)