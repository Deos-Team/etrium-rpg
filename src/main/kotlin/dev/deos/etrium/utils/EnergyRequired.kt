package dev.deos.etrium.utils

enum class EnergyRequired(
    val value: Float
) {
    BlockBreaking(0.1f),
    AttackEntity(1.0f)
}