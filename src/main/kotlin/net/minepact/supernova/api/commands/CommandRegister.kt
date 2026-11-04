package net.minepact.supernova.api.commands

import net.minepact.api.command.Result
import net.minepact.supernova.Main
import net.minepact.supernova.api.commands.arguments.Argument
import net.minepact.supernova.api.commands.arguments.parseArgument
import net.minepact.supernova.api.logger.LogType
import net.minepact.supernova.api.logger.Logger
import org.bukkit.Bukkit
import org.bukkit.command.Command as BukkitCommand
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

import kotlin.*
import kotlin.collections.*
import kotlin.sequences.*
import kotlin.text.*
import kotlin.jvm.javaClass
import java.util.UUID

class CommandRegister {
    private val COMMANDS: MutableList<Command> = mutableListOf()
    private val RAN_COMMANDS: MutableMap<CommandSender, MutableMap<Command, Long>> = mutableMapOf()

    fun register(command: Command) {
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
                /*
                if ((sender is Player && sender.player?.hasPermission(command.permission.node) == true) && !sender.hasPermission("nexus.command.bypass") && !sender.isOp && sender !is ConsoleCommandSender) {
                    sender.sendMessage("<red>You do not have permission to execute this command.")
                    return true
                }

                if (command.playerOnly && sender !is Player) {
                    sender.sendMessage("<red>This command can only be used by players.")
                    return true
                }
                 */

                val parsedArgs = mutableListOf<Argument<*>>()
                for (index in args.indices) {
                    val expectedArguments = command.usage.arguments

                    if (expectedArguments.isEmpty()) {
                        sender.sendMessage("<red>Invalid arguments. Usage: ${command.usage}")
                        return true
                    }
                    val parsed = expectedArguments
                        .filter { expected -> expected.permission?.let(sender::hasPermission) ?: true }
                        .filter { expected -> expected.senderFilter?.invoke(sender) ?: true }
                        .firstNotNullOfOrNull { expected -> parseArgument(args[index], expected) }
                    if (parsed == null) {
                        sender.sendMessage("<red>Invalid arguments. Usage: ${command.usage}")
                        return true
                    }

                    parsedArgs.add(parsed)
                }
                val missingRequired = command.usage.arguments
                    .drop(parsedArgs.size)
                    .any { !it.optional }

                if (missingRequired) {
                    sender.sendMessage("<red>Invalid arguments. Usage: ${command.usage}")
                    return true
                }

                return try {
                    if (RAN_COMMANDS.contains(sender) && RAN_COMMANDS[sender]!!.contains(command)) {
                        val lastRan = RAN_COMMANDS[sender]!![command]!!
                        val cooldownMillis = (1 /* PLACEHOLDER COOLDOWN TODO */ * 1000).toLong()
                        if ((System.currentTimeMillis() - lastRan < cooldownMillis) && !sender.hasPermission("nexus.cooldown.bypass")) {
                            val timeLeft: Double = ((cooldownMillis - (System.currentTimeMillis() - lastRan)) / 1000.0)
                            sender.sendMessage("<red>You must wait $timeLeft seconds before using this command again.")
                            return true
                        }
                    }

                    // RAN_COMMANDS[sender] set RAN_COMMANDS.getOrDefault(sender, mutableMapOf())
                    //     .also { it[command] set System.currentTimeMillis() }

                    /*
                    sender.asPlayer().writeLog(LogInfo(
                        serverId = Main.SERVER.info.uuid,
                        senderId = (sender as? Player)?.uniqueId ?: UUID(0, 0),
                        type = LogType.COMMAND,
                        timestamp = System.currentTimeMillis(),
                        content = "/${command.name} ${parsedArgs.joinToString(" ") { it.value.toString() }}",
                        suspicious = command.log
                    ))
                     */

                    command.execute(sender, parsedArgs) == Result.SUCCESS
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }

            override fun tabComplete(
                sender: CommandSender,
                alias: String,
                args: Array<String>
            ): MutableList<String> {
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

        // if (command.server == Main.SERVER.info.type || command.server == ServerType.GLOBAL) {
            this.getCommandMap().register(Main.instance.name.lowercase(), bukkitCommand)
            COMMANDS.add(command)
            Logger.log("[CommandRegister] Registered ${command.javaClass.name}")
        // }
    }
    fun unregister(command: Command) {
        this.getCommandMap().knownCommands.remove("${Main.instance.name.lowercase()}:${command.name}")
        this.getCommandMap().knownCommands.remove(command.name)

        COMMANDS.remove(command)
        Main.instance.logger.info("[CommandRegister] Unregistered ${command.javaClass.name}!")
    }
    fun unregister(name: String) {
        this.getCommandMap().knownCommands.remove("${Main.instance.name.lowercase()}:${name}")
        this.getCommandMap().knownCommands.remove(name)

        Main.instance.logger.info("[CommandRegister] Unregistered ${COMMANDS.first { it.name == name }.javaClass.name}!")
        COMMANDS.forEach { if (it.name == name) COMMANDS.remove(it) }
    }

    fun all(): List<Command> = COMMANDS
    fun allNames(): List<String> = COMMANDS.map { it.name }

    fun getCommandMap(): CommandMap {
        val field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        field.isAccessible = true
        return field.get(Bukkit.getServer()) as CommandMap
    }
}