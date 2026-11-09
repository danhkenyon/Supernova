package net.minepact.supernova.api.minecraft.spawn

data class SpawnData(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
)