package net.minepact.supernova.api.commands.arguments

import org.bukkit.command.CommandSender

class ArgumentContext(
    val sender: CommandSender,
    private val arguments: Map<String, Argument<*>>
) {
    fun argument(name: String): Argument<*>? = arguments[name]
    fun value(name: String): Any? = arguments[name]?.value
    inline fun <reified T : Any> get(name: String): T? = value(name) as? T

    fun contains(name: String): Boolean = arguments.containsKey(name)
    fun all(): Map<String, Argument<*>> = arguments.toMap()
}