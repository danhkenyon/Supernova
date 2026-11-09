package net.minepact.supernova.api.config

data class ConfigValue<T>(
    val key: String,
    val value: T
)