package net.minepact.supernova.api.permissions

data class Permission(
    val node: String,
    val value: Boolean = true
)