package net.minepact.supernova.api.events

class EventExecutionException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)