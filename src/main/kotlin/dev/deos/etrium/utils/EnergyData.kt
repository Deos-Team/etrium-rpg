package dev.deos.etrium.utils

object EnergyData {
    fun addEnergy(player: IEntityDataSaver, amount: Float): Float {
        val nbt = player.getPersistentData()
        var energy = nbt.getFloat("energy")

        if (energy + amount > nbt.getFloat("maxEnergy")) {
            energy = nbt.getFloat("maxEnergy")
        } else {
            energy += amount
        }

        nbt.putFloat("energy", energy)

        return energy
    }

    fun removeEnergy(player: IEntityDataSaver, amount: Float): Float {
        val nbt = player.getPersistentData()
        var energy = nbt.getFloat("energy")

        if (energy - amount < 0) {
            energy = 0F
        } else {
            energy -= amount
        }

        nbt.putFloat("energy", energy)

        return energy
    }

    fun getEnergy(player: IEntityDataSaver): Float {
        val nbt = player.getPersistentData()
        return nbt.getFloat("energy")
    }

    fun getMaxEnergy(player: IEntityDataSaver): Float {
        val nbt = player.getPersistentData()
        return nbt.getFloat("maxEnergy")
    }

    fun getRegen(player: IEntityDataSaver): Float {
        val nbt = player.getPersistentData()
        return nbt.getFloat("regen")
    }
}