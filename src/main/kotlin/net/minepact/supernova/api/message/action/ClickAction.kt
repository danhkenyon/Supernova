package net.minepact.supernova.api.message.action

object ClickAction {
    fun runCommand(command: String) = MessageAction.Click(ClickType.RUN_COMMAND, command)
    fun suggestCommand(command: String) = MessageAction.Click(ClickType.SUGGEST_COMMAND, command)
    fun copyToClipboard(text: String) = MessageAction.Click(ClickType.COPY_TO_CLIPBOARD, text)
    fun openUrl(url: String): MessageAction {
        require(url.startsWith("https://") || url.startsWith("http://")) { "Invalid URL: $url" }
        return MessageAction.Click(ClickType.OPEN_URL, url)
    }
}