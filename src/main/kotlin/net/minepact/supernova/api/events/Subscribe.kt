package net.minepact.supernova.api.events

import net.minepact.supernova.api.server.ServerType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Subscribe(
    val priority: EventPriority = EventPriority.NORMAL,
    val ignoreCancelled: Boolean = true,
    val server: ServerType = ServerType.GLOBAL
)