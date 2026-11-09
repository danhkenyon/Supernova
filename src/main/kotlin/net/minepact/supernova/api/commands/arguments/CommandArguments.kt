package net.minepact.supernova.api.commands.arguments

class CommandArguments(
    arguments: Collection<Argument<*>>
) {
    @PublishedApi
    internal val arguments: Map<String, Argument<*>> = arguments.associateBy { it.name }

    init {
        require(this.arguments.size == arguments.size) { "Command arguments must have unique names." }
    }

    inline fun <reified T : Any> get(name: String): T {
        val argument = arguments[name] ?: throw NoSuchElementException("Argument '$name' does not exist.")
        return argument.value as? T
            ?: throw IllegalStateException("Argument '$name' is not of type ${T::class.simpleName}.")
    }

    inline fun <reified T : Any> getOrNull(name: String): T? {
        val argument = arguments[name] ?: return null
        return argument.value as? T
            ?: throw IllegalStateException("Argument '$name' is not of type ${T::class.simpleName}.")
    }

    fun contains(name: String): Boolean = name in arguments
    fun argument(name: String): Argument<*>? = arguments[name]
    fun all(): Map<String, Argument<*>> = arguments.toMap()
}