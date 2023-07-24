package dev.deos.etrium.utils

enum class EnergyRequired(
    val value: Float
) {
    blockBreaking(1.0f),
    attackEntity(5.0f)
}