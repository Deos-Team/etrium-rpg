package dev.deos.etrium.utils

enum class EnergyRequired(
    val value: Float
) {
    blockBreaking(0.5f),
    attackEntity(1.0f)
}