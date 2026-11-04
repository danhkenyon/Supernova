package net.minepact.supernova.api.message.action

sealed interface MessageAction {
    data class Hover(val text: String) : MessageAction
    data class Click(val type: ClickType, val value: String) : MessageAction
}