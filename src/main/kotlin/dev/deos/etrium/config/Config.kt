package dev.deos.etrium.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val default: Default = Default()
)

@Serializable
data class Default(
    val energy: Float = 50f,
    val maxEnergy: Float = 100f,
    val regen: Float = 1.0f
)

