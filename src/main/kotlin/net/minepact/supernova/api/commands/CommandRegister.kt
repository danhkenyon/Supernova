package net.minepact.supernova.api.commands

import net.minepact.supernova.Main
import net.minepact.supernova.api.commands.arguments.Argument
import net.minepact.supernova.api.commands.arguments.ArgumentContext
import net.minepact.supernova.api.commands.arguments.ArgumentParser
import net.minepact.supernova.api.commands.arguments.CommandArguments
import net.minepact.supernova.api.logger.LogType
import net.minepact.supernova.api.logger.Logger
import net.minepact.supernova.api.message.MessageBuilder
import net.minepact.supernova.api.message.helpers.Messages.ERROR_PREFIX
import net.minepact.supernova.api.message.sendMessage
import net.minepact.supernova.api.server.ServerType
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.ceil
import org.bukkit.command.Command as BukkitCommand

class CommandRegister {
    private val COMMAND_PREFIX: String = "nexus"
    private val COMMANDS: MutableList<Command> = mutableListOf()
    private val RAN_COMMANDS: MutableMap<UUID, MutableMap<Command, Long>> = mutableMapOf()
    private val REGISTERED_COMMANDS: MutableMap<Command, BukkitCommand> = mutableMapOf()

    fun register(command: Command) {
        if (command in COMMANDS) return

        val bukkitCommand = object : BukkitCommand(
            command.name,
            command.description,
            command.usage.toString(),
            command.aliases
        ) {
            override fun execute(
                sender: CommandSender,
                label: String,
                args: Array<String>
            ): Boolean {
                if (
                    !sender.hasPermission(command.permission.node) &&
                    !sender.hasPermission("nexus.command.bypass") &&
                    !sender.isOp
                ) {
                    sender.sendMessage(
                        MessageBuilder()
                            .append("<red>You do not have permission to execute this command.")
                            .build()
                    )
                    return true
                }
                if (command.playerOnly && sender !is Player) {
                    sender.sendMessage(
                        MessageBuilder().append("<red>This command can only be used by players.").build()
                    )
                    return true
                }

                val parsedArgs = mutableMapOf<String, Argument<*>>()
                for (index in args.indices) {
                    val expected = command.usage.arguments.getOrNull(index)
                    if (expected == null) {
                        sender.sendMessage(
                            MessageBuilder().append("<red>Too many arguments. Usage: ${command.usage}").build()
                        )
                        return true
                    }

                    if (expected.permission?.let(sender::hasPermission) == false) {
                        sender.sendMessage(
                            MessageBuilder().append("<red>You do not have permission to use this argument.").build()
                        )
                        return true
                    }
                    if (expected.senderFilter?.invoke(sender) == false) {
                        sender.sendMessage(MessageBuilder().append("<red>You cannot use this argument.").build())
                        return true
                    }

                    val parsed = ArgumentParser.parse(args[index], expected, ArgumentContext(sender, parsedArgs))
                    if (parsed == null) {
                        sender.sendMessage(
                            MessageBuilder().append("<red>Invalid argument. Usage: ${command.usage}").build()
                        )
                        return true
                    }

                    parsedArgs[parsed.name] = parsed
                }
                val missingRequired = command.usage.arguments
                    .drop(parsedArgs.size)
                    .any { !it.optional }

                if (missingRequired) {
                    sender.sendMessage(
                        MessageBuilder().append("<red>Invalid arguments. Usage: ${command.usage}").build()
                    )
                    return true
                }

                return try {
                    val now = System.currentTimeMillis()
                    val lastRan = RAN_COMMANDS[(sender as? Player)?.uniqueId ?: UUID(0, 0)]?.get(command)

                    if (lastRan != null && now - lastRan < command.cooldown * 1000L && !sender.hasPermission("nexus.cooldown.bypass")) {
                        val timeLeft = ceil((command.cooldown - (now - lastRan)) / 1000.0).toLong()
                        sender.sendMessage(
                            MessageBuilder().append("<red>You must wait $timeLeft seconds before using this command again.")
                                .build()
                        )
                        return true
                    }

                    when (command.execute(sender, CommandArguments(parsedArgs.values))) {
                        CommandResult.SUCCESS -> {
                            RAN_COMMANDS.getOrPut(
                                (sender as? Player)?.uniqueId ?: UUID(
                                    0,
                                    0
                                )
                            ) { mutableMapOf() }[command] = now
                            true
                        }

                        CommandResult.FAILURE -> {
                            Logger.log(
                                "Command /${command.name} ${parsedArgs.values.joinToString(separator = " ")} failed to execute.",
                                LogType.WARN
                            )
                            Logger.log("Please locate the error and attempt to fix.", LogType.WARN)
                            true
                        }

                        CommandResult.NO_PERMISSION -> {
                            sender.sendMessage(
                                ERROR_PREFIX.append("<red>You do not have permission to run this command.").build()
                            )
                            true
                        }
                    }
                } catch (e: Exception) {
                    Logger.log(
                        "An exception occurred while executing /${command.name}: ${e.message}",
                        LogType.ERROR
                    )

                    e.printStackTrace()
                    true
                }
            }

            override fun tabComplete(
                sender: CommandSender,
                alias: String,
                args: Array<String>
            ): MutableList<String> {
                if (
                    !sender.hasPermission(command.permission.node) &&
                    !sender.hasPermission("nexus.command.bypass") &&
                    !sender.isOp
                ) return mutableListOf()

                val index = args.size - 1
                val current = args.lastOrNull()?.lowercase() ?: ""
                val expected = command.usage.arguments.getOrNull(index) ?: return mutableListOf()

                return sequenceOf(expected)
                    .filter { it.permission?.let(sender::hasPermission) ?: true }
                    .filter { it.senderFilter?.invoke(sender) ?: true }
                    .flatMap { arg ->
                        when {
                            arg.dynamicProvider != null -> arg.dynamicProvider.invoke(sender).asSequence()
                            arg.potentialValues != null -> arg.potentialValues?.asSequence() ?: emptySequence()
                            else -> emptySequence()
                        }
                    }
                    .filter { it.lowercase().startsWith(current) }
                    .distinct()
                    .toMutableList()
            }
        }

        if (command.server == Main.serverInfo.type || command.server == ServerType.GLOBAL) {
            this.getCommandMap().register(COMMAND_PREFIX, bukkitCommand)
            COMMANDS.add(command)
            Logger.log("[CommandRegister] Registered ${command.javaClass.simpleName}")
        }
    }
    fun unregister(command: Command) {
        val bukkitCommand = REGISTERED_COMMANDS.remove(command) ?: return
        getCommandMap().knownCommands.entries.removeIf { it.value === bukkitCommand }

        COMMANDS.remove(command)
        Logger.log("[CommandRegister] Unregistered ${command.javaClass.name}")
    }
    fun unregister(name: String) {
        val command = COMMANDS.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: return
        unregister(command)
    }

    fun unregisterAll() {
        COMMANDS.forEach { unregister(it) }
    }

    fun all(): List<Command> = COMMANDS.toList()
    fun allNames(): List<String> = COMMANDS.map { it.name }

    fun getCommandMap(): CommandMap {
        val field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        field.isAccessible = true
        return field.get(Bukkit.getServer()) as CommandMap
    }
}