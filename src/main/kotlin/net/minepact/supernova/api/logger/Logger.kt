package net.minepact.supernova.api.logger

import net.minepact.supernova.Main
import java.util.logging.Level

object Logger {
    fun log(msg: String, type: LogType = LogType.INFO) = when (type) {
            LogType.WARN -> Main.instance.logger.log(Level.WARNING, msg)
            LogType.INFO -> Main.instance.logger.log(Level.INFO, msg)
            LogType.ERROR -> Main.instance.logger.log(Level.SEVERE, msg)
            LogType.DEBUG -> Main.instance.logger.log(Level.INFO, "[DEBUG] $msg")
    }
}