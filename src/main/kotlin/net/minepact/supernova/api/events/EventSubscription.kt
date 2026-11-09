package net.minepact.supernova.api.events

import net.minepact.supernova.api.server.ServerType
import java.lang.reflect.Method

data class EventSubscription(
    val listener: EventListener,
    val method: Method,
    val eventClass: Class<*>,
    val priority: EventPriority,
    val ignoreCancelled: Boolean,
    val server: ServerType
) {
    fun execute(event: Any) {
        if (!eventClass.isInstance(event)) return

        if (
            ignoreCancelled &&
            event is Cancellable &&
            event.isCancelled
        ) return

        try {
            method.invoke(listener, event)
        } catch (exception: Exception) {
            throw EventExecutionException(
                "Failed to execute ${method.name} in ${listener.javaClass.simpleName}",
                exception.cause ?: exception
            )
        }
    }
}