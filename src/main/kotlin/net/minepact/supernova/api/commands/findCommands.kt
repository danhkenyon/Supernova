package net.minepact.supernova.api.commands

import net.minepact.supernova.Main

import java.io.File
import java.lang.reflect.Modifier
import java.util.jar.JarFile
import kotlin.apply
import kotlin.io.use
import kotlin.jvm.*
import kotlin.sequences.*
import kotlin.text.*

fun findCommands(basePackage: String): List<Command> {
    val commands = mutableListOf<Command>()
    val basePath = basePackage.replace('.', '/')

    val jarFile = JarFile(File(Main.instance.javaClass.protectionDomain.codeSource.location.toURI()))
    jarFile.use { jar ->
        jar.entries().asSequence()
            .filter { it.name.endsWith(".class") }
            .filter { it.name.startsWith(basePath) }
            .forEach { entry ->
                val className = entry.name
                    .removeSuffix(".class")
                    .replace('/', '.')

                try {
                    val clazz = Class.forName(className)
                    if (
                        Command::class.java.isAssignableFrom(clazz) &&
                        !clazz.isInterface &&
                        !Modifier.isAbstract(clazz.modifiers)
                    ) {
                        val instance = clazz
                            .getDeclaredConstructor()
                            .apply { isAccessible = true }
                            .newInstance() as Command
                        commands.add(instance)
                    }
                } catch (_: Throwable) {}
            }
    }
    return commands
}