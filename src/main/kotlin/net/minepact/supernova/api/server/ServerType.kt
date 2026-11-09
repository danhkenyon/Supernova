package net.minepact.supernova.api.server

enum class ServerType {
    GLOBAL,
    HUB,
    BOX;

    companion object {
        fun fromName(name: String): ServerType {
            return when (name) {
                "GLOBAL" -> GLOBAL
                "HUB" -> HUB
                "BOX" -> BOX
                else -> DEFAULT_SERVER_TYPE
            }
        }
    }
}