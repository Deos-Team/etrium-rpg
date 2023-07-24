package dev.deos.etrium.utils

interface EnergyContainer {
    fun getEnergy(): Float

    fun setEnergy(value: Float)

    fun getMaxEnergy(): Float

    fun setMaxEnergy(value: Float)

    fun getRegenEnergy(): Float

    fun setRegenEnergy(value: Float)

}