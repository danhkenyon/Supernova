package net.minepact.supernova.api.message

import net.minepact.supernova.api.message.action.Action
import net.minepact.supernova.api.message.action.ClickAction
import net.minepact.supernova.api.message.action.HoverAction
import net.minepact.supernova.api.message.action.MessageAction

class MessageSegmentBuilder {
    internal val actions = mutableListOf<MessageAction>()

    fun action(action: Action, text: String) {
        when (action) {
            Action.SHOW_TEXT -> hover(text)
            Action.COPY_TO_CLIPBOARD -> copyToClipboard(text)
            Action.RUN_COMMAND -> runCommand(text)
            Action.SUGGEST_COMMAND -> suggestCommand(text)
            Action.OPEN_URL -> openUrl(text)
        }
    }

    private fun action(action: MessageAction) { actions.add(action) }
    private fun hover(text: String) { action(HoverAction.showText(text)) }
    private fun copyToClipboard(text: String) { action(ClickAction.copyToClipboard(text)) }
    private fun runCommand(command: String) { action(ClickAction.runCommand(command)) }
    private fun suggestCommand(command: String) { action(ClickAction.suggestCommand(command)) }
    private fun openUrl(url: String) { action(ClickAction.openUrl(url)) }
}