package net.minepact.supernova.api.items

import net.minepact.supernova.Main
import net.minepact.supernova.api.events.EventListener
import net.minepact.supernova.api.logger.LogType
import net.minepact.supernova.api.logger.Logger
import java.io.File
import java.lang.reflect.Modifier
import java.util.jar.*

fun findItems(basePackage: String): List<Item> {
    val items = mutableListOf<Item>()
    val basePath = basePackage.replace('.', '/')
    val jarFile = JarFile(File(Main.instance.javaClass.protectionDomain.codeSource.location.toURI()))

    jarFile.use { jar ->
        jar.entries().asSequence()
            .filter { it.name.endsWith(".class") }
            .filter { it.name.startsWith("$basePath/") }
            .forEach { entry ->
                val className = entry.name
                    .removeSuffix(".class")
                    .replace('/', '.')

                try {
                    val clazz = Class.forName(
                        className,
                        false,
                        Main.instance.javaClass.classLoader
                    )

                    if (
                        Item::class.java.isAssignableFrom(clazz) &&
                        !clazz.isInterface &&
                        !Modifier.isAbstract(clazz.modifiers)
                    ) {
                        val instance = clazz
                            .getDeclaredConstructor()
                            .apply { isAccessible = true }
                            .newInstance() as Item

                        items.add(instance)
                    }
                } catch (exception: Exception) {
                    Logger.log("Could not discover listener $className: ${exception.message}", LogType.WARN)
                }
            }
    }

    return items
}