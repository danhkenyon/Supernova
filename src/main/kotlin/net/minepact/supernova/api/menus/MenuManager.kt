package net.minepact.supernova.api.menus

import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object MenuManager {
    private val sessions = ConcurrentHashMap<UUID, MenuSession>()
    private var initialized = false

    @Synchronized
    fun initialize() {
        if (initialized) return
        initialized = true
    }

    fun open(player: Player, menu: Menu): MenuSession {
        require(initialized) { "MenuManager has not been initialized" }
        close(player)

        val session = MenuSession(player, menu)
        sessions[player.uniqueId] = session

        session.display()
        menu.onOpen(session)

        return session
    }

    fun close(player: Player) {
        val session = sessions[player.uniqueId] ?: return

        if (session.isOpen) player.closeInventory()
        else removeSession(session)
    }

    fun session(player: Player): MenuSession? {
        return sessions[player.uniqueId]
    }
    fun hasSession(player: Player): Boolean {
        return sessions.containsKey(player.uniqueId)
    }
    internal fun removeSession(session: MenuSession) {
        if (sessions.remove(session.playerId, session)) session.menu.onClose(session)
    }

    fun shutdown() {
        sessions.values.toList().forEach { close(it.player) }

        sessions.clear()
        initialized = false
    }
}