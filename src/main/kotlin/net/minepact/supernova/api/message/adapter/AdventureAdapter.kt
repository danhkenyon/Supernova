package net.minepact.supernova.api.message.adapter

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minepact.supernova.api.message.Message
import net.minepact.supernova.api.message.MessageSegment
import net.minepact.supernova.api.message.action.ClickType
import net.minepact.supernova.api.message.action.MessageAction

object AdventureAdapter {
    fun convert(message: Message): Component = message.segments.fold(Component.empty()) { result, segment ->
        result.append(convert(segment))
    }

    private fun convert(segment: MessageSegment): Component {
        var component: Component = Component.text(segment.text)
        val style = segment.style
        style.color?.let { component = component.color(TextColor.color(it.red, it.green, it.blue)) }

        val decorations = mapOf(
            TextDecoration.BOLD to style.bold,
            TextDecoration.ITALIC to style.italic,
            TextDecoration.UNDERLINED to style.underline,
            TextDecoration.STRIKETHROUGH to style.strikethrough,
            TextDecoration.OBFUSCATED to style.obfuscated
        )

        decorations.forEach { (decoration, enabled) -> component = component.decoration(decoration, enabled) }
        segment.actions.forEach { action ->
            component = when (action) {
                is MessageAction.Hover -> component.hoverEvent(
                    HoverEvent.showText(parseToComponent(action.text))
                )

                is MessageAction.Click -> {
                    val event = when (action.type) {
                        ClickType.RUN_COMMAND -> ClickEvent.runCommand(action.value)
                        ClickType.SUGGEST_COMMAND -> ClickEvent.suggestCommand(action.value)
                        ClickType.OPEN_URL -> ClickEvent.openUrl(action.value)
                        ClickType.COPY_TO_CLIPBOARD -> ClickEvent.copyToClipboard(action.value)
                    }
                    component.clickEvent(event)
                }
            }
        }

        return component
    }

    private fun parseToComponent(text: String): Component {
        val segments = net.minepact.supernova.api.message.parser.MessageParser().parse(text)
        return segments.fold(Component.empty()) { result, segment ->
            result.append(convert(segment))
        }
    }
}