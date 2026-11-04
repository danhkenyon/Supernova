package net.minepact.supernova.api.message

import net.kyori.adventure.text.Component
import net.minepact.supernova.api.message.action.MessageAction
import net.minepact.supernova.api.message.parser.MessageParser

class MessageBuilder {

    private val segments = mutableListOf<MessageSegment>()

    fun append(
        text: String,
        block: MessageSegmentBuilder.() -> Unit = {}
    ): MessageBuilder {
        val builder = MessageSegmentBuilder()
        builder.block()

        segments.addAll(MessageParser().parse(
                text,
                builder.actions
        ))

        return this
    }

    fun append(message: Message): MessageBuilder {
        segments.addAll(message.segments)
        return this
    }

    fun build(): Message =
        Message(segments.toList())
}