package net.minepact.supernova.api.message.action

object HoverAction {
    fun showText(text: String) = MessageAction.Hover(text)
}