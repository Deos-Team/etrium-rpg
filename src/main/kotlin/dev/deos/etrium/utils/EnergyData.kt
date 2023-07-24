package dev.deos.etrium.utils

import net.minecraft.nbt.NbtCompound

object EnergyData {
    fun addEnergy(player: IEntityDataSaver, amount: Float): Float {
        val nbt = player.getPersistentData()
        var energy = player.getEnergy()

        if (energy + amount > player.getMaxEnergy()) {
            energy = player.getMaxEnergy()
        } else {
            energy += amount
        }

        nbt.putFloat("energy", energy)

        return energy
    }

    fun removeEnergy(player: IEntityDataSaver, amount: Float): Float {
        val nbt = player.getPersistentData()
        var energy = player.getEnergy()

        if (energy - amount < 0) {
            energy = 0F
        } else {
            energy -= amount
        }

        nbt.putFloat("energy", energy)

        return energy
    }

    fun IEntityDataSaver.getEnergy(): Float {
        val nbt = this.getPersistentData()
        return nbt.getFloat("energy")
    }

    fun IEntityDataSaver.getMaxEnergy(): Float {
        val nbt = this.getPersistentData()
        return nbt.getFloat("maxEnergy")
    }

    fun IEntityDataSaver.getRegen(): Float {
        val nbt = this.getPersistentData()
        return nbt.getFloat("regen")
    }
}

interface IEntityDataSaver {
    fun getPersistentData(): NbtCompound
}