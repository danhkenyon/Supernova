package net.minepact.supernova.api.message.render

import net.minepact.supernova.api.message.Message
import net.minepact.supernova.api.message.MessageTarget
import java.util.concurrent.ConcurrentHashMap

object MessageRenderers {
    private val renderers = ConcurrentHashMap<MessageTarget, MessageRenderer<*>>()

    fun <T : Any> register(target: MessageTarget, renderer: MessageRenderer<T>) {
        renderers[target] = renderer
    }
    fun unregister(target: MessageTarget) {
        renderers.remove(target)
    }
    fun hasRenderer(target: MessageTarget): Boolean {
        return renderers.containsKey(target)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(target: MessageTarget): MessageRenderer<T>? {
        return renderers[target] as? MessageRenderer<T>
    }
    fun <T : Any> render(target: MessageTarget, message: Message): T {
        val renderer = get<T>(target) ?: error("No renderer registered for $target")
        return renderer.render(message)
    }

    fun clear() {
        renderers.clear()
    }
}