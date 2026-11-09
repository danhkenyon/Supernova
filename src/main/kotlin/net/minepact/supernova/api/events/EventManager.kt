package net.minepact.supernova.api.events

import net.minepact.supernova.Main
import net.minepact.supernova.api.logger.LogType
import net.minepact.supernova.api.logger.Logger
import net.minepact.supernova.api.server.ServerType
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import java.lang.reflect.Modifier
import org.bukkit.event.EventPriority as BukkitPriority

class EventManager {
    private val listeners = mutableListOf<EventListener>()
    private val subscriptions = mutableListOf<EventSubscription>()
    private val customSubscriptions = mutableMapOf<Class<out NexusEvent>, MutableList<EventSubscription>>()
    private val bukkitAdapters = mutableMapOf<EventListener, Listener>()
    private var registrationCounter = 0L

    fun register(listener: EventListener) {
        if (listener in listeners) return

        val methods = listener.javaClass.methods.filter { it.isAnnotationPresent(Subscribe::class.java) }
        val discovered = methods.map { method ->
            validateMethod(method)

            val annotation = method.getAnnotation(Subscribe::class.java)
            val eventClass = method.parameterTypes[0]

            EventSubscription(
                listener = listener,
                method = method,
                eventClass = eventClass,
                priority = annotation.priority,
                ignoreCancelled = annotation.ignoreCancelled,
                server = annotation.server
            )
        }.filter { it.server == ServerType.GLOBAL || it.server == Main.serverInfo.type }

        if (discovered.isEmpty()) return

        val custom = discovered.filter { NexusEvent::class.java.isAssignableFrom(it.eventClass) }
        val bukkit = discovered.filter { Event::class.java.isAssignableFrom(it.eventClass) }

        val unsupported = discovered - custom.toSet() - bukkit.toSet()
        require(unsupported.isEmpty()) { "Event handlers must accept SupernovaEvent or Bukkit Event types." }

        if (bukkit.isNotEmpty()) {
            val adapter = object : Listener {}
            bukkitAdapters[listener] = adapter

            bukkit.forEach { registerBukkitEvent(adapter, it) }
        }

        custom.forEach { subscription ->
            @Suppress("UNCHECKED_CAST")
            val eventClass = subscription.eventClass as Class<out NexusEvent>
            customSubscriptions
                .getOrPut(eventClass) { mutableListOf() }
                .add(subscription)
        }

        subscriptions.addAll(discovered)
        listeners.add(listener)

        registrationCounter++

        Logger.log("[EventManager] Registered ${listener.javaClass.simpleName} (${discovered.size} handlers)")
    }

    private fun validateMethod(method: java.lang.reflect.Method) {
        require(method.parameterCount == 1) { "Event handler ${method.name} must have exactly one parameter." }
        require(!Modifier.isStatic(method.modifiers)) { "Event handler ${method.name} cannot be static." }
        require(!Modifier.isAbstract(method.modifiers)) { "Event handler ${method.name} cannot be abstract." }
        require(method.returnType == Void.TYPE) { "Event handler ${method.name} must return Unit." }
        require(method.parameterTypes[0] != Any::class.java) { "Event handler ${method.name} must specify an event type." }
    }

    private fun registerBukkitEvent(adapter: Listener, subscription: EventSubscription) {
        @Suppress("UNCHECKED_CAST")
        val eventClass = subscription.eventClass as Class<out Event>

        val priority = when (subscription.priority) {
            EventPriority.LOWEST -> BukkitPriority.LOWEST
            EventPriority.LOW -> BukkitPriority.LOW
            EventPriority.NORMAL -> BukkitPriority.NORMAL
            EventPriority.HIGH -> BukkitPriority.HIGH
            EventPriority.HIGHEST -> BukkitPriority.HIGHEST
            EventPriority.MONITOR -> BukkitPriority.MONITOR
        }

        val executor = EventExecutor { _, event ->
            try {
                subscription.execute(event)
            } catch (exception: Exception) {
                Logger.log("[EventManager] ${exception.message}", LogType.ERROR)
                exception.printStackTrace()
            }
        }

        Bukkit.getPluginManager().registerEvent(
            eventClass,
            adapter,
            priority,
            executor,
            Main.instance,
            subscription.ignoreCancelled
        )
    }

    fun call(event: NexusEvent): NexusEvent {
        val handlers = customSubscriptions
            .filterKeys { it.isAssignableFrom(event.javaClass) }
            .values
            .flatten()
            .sortedBy { it.priority.ordinal }

        for (subscription in handlers) {
            try {
                subscription.execute(event)
            } catch (exception: Exception) {
                Logger.log("[EventManager] ${exception.message}", LogType.ERROR)
                exception.printStackTrace()
            }
        }

        return event
    }

    fun unregister(listener: EventListener) {
        if (!listeners.remove(listener)) return

        subscriptions.removeIf { it.listener === listener }
        customSubscriptions.values.forEach { handlers ->
            handlers.removeIf { it.listener === listener }
        }
        customSubscriptions.entries.removeIf { it.value.isEmpty() }
        bukkitAdapters.remove(listener)?.let { org.bukkit.event.HandlerList.unregisterAll(it) }

        Logger.log("[EventManager] Unregistered ${listener.javaClass.simpleName}")
    }

    fun unregisterAll() {
        listeners.toList().forEach(::unregister)
    }

    fun all(): List<EventListener> = listeners.toList()
    fun allHandlers(): List<EventSubscription> = subscriptions.toList()

    fun size(): Int = listeners.size
    fun handlerCount(): Int = subscriptions.size
}