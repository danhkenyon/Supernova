package net.minepact.supernova.core.global.commands

import net.minepact.supernova.api.commands.Command
import net.minepact.supernova.api.commands.CommandResult
import net.minepact.supernova.api.commands.CommandUsage
import net.minepact.supernova.api.commands.arguments.CommandArguments
import net.minepact.supernova.api.commands.arguments.ExpectedArgument
import net.minepact.supernova.api.commands.arguments.Provider
import net.minepact.supernova.api.commands.arguments.types.primative.StringArgumentType
import net.minepact.supernova.api.config.ConfigFile
import net.minepact.supernova.api.config.ConfigRegistry
import net.minepact.supernova.api.config.ConfigValue
import net.minepact.supernova.api.message.helpers.Messages.ERROR_PREFIX
import net.minepact.supernova.api.message.helpers.Messages.NEXUS_PREFIX
import net.minepact.supernova.api.message.sendMessage
import net.minepact.supernova.api.permissions.Permission
import org.bukkit.command.CommandSender

class ConfigCommand : Command(
    name = "config",
    description = "Manage configuration files.",
    permission = Permission("nexus.admin.config"),
    aliases = emptyList(),
    usage = CommandUsage(
        label = "config",
        arguments = listOf<ExpectedArgument<*>>(
            ExpectedArgument(
                name = "path",
                type = StringArgumentType,
                contextualProvider = { context -> Provider.CONFIG_FILES(context.sender) }
            ),
            ExpectedArgument(
                name = "action",
                type = StringArgumentType,
                potentialValues = listOf(
                    "set-value",
                    "get-value",
                    "remove-value",
                    "reload"
                )
            ),
            ExpectedArgument(
                name = "key",
                type = StringArgumentType,
                optional = true,
                requiredWhen = {
                    it.get<String>("action") in listOf(
                        "set-value",
                        "get-value",
                        "remove-value"
                    )
                }
            ),
            ExpectedArgument(
                name = "value",
                type = StringArgumentType,
                optional = true,
                consumeRemaining = true,
                requiredWhen = { it.get<String>("action") == "set-value" }
            )
        )
    )
) {
    override fun execute(sender: CommandSender, args: CommandArguments): CommandResult {
        val path: String = args.get("path")
        val action: String = args.get("action")

        val configName = path.substringBeforeLast('.')
        val config = ConfigRegistry.get(configName)

        if (config == null) {
            sender.sendMessage(
                ERROR_PREFIX.append("<red>The file you entered is not registered. If this is an error, contact a plugin developer.")
                    .build()
            )
            return CommandResult.FAILURE
        }

        return when (action) {
            "set-value" -> {
                val (key, value) = Pair(args.get<String>("key"), args.get<Any>("value"))

                config.setValue(ConfigValue(key, value))
                sender.sendMessage(
                    NEXUS_PREFIX.append("<green>Set the value of <white>${key}</white> to <white>${value}</white>. [<white>${path}</white>]")
                        .build()
                )

                CommandResult.SUCCESS
            }

            "get-value" -> {
                val (key, value) = getKeyIfNotNull(sender, args, config, path)
                if (value == null) return CommandResult.FAILURE

                sender.sendMessage(
                    NEXUS_PREFIX.append("<green>The value of <white>${key}</white> is <white>${value.value}</white>. [<white>${path}</white>]")
                        .build()
                )
                CommandResult.SUCCESS
            }

            "remove-value" -> {
                val (key, value) = getKeyIfNotNull(sender, args, config, path)
                if (value == null) return CommandResult.FAILURE

                config.removeValue(key)
                sender.sendMessage(
                    NEXUS_PREFIX.append("<green>Removed <white>${key}</white>. [<white>${path}</white>]").build()
                )
                CommandResult.SUCCESS
            }

            "reload" -> {
                val startTime: Long = System.currentTimeMillis()
                sender.sendMessage(NEXUS_PREFIX.append("<yellow>Reloading <white>${path}</white>...").build())
                config.reload()
                sender.sendMessage(
                    NEXUS_PREFIX.append("<green>Reloaded <white>${path}</white> in <white>${System.currentTimeMillis() - startTime}</white>ms.")
                        .build()
                )
                CommandResult.SUCCESS
            }

            else -> CommandResult.FAILURE
        }
    }

    private fun getKeyIfNotNull(
        sender: CommandSender,
        args: CommandArguments,
        config: ConfigFile,
        path: String
    ): Pair<String, ConfigValue<Any>?> {
        val key: String = args.get("key")
        val value = config.getValue(key)

        if (value == null) {
            sender.sendMessage(
                ERROR_PREFIX.append("<red>The key '<white>${key}</white>' is not present. [<white>${path}</white>]")
                    .build()
            )
        }
        return Pair(key, value)
    }
}