package dev.deos.etrium.utils

import dev.deos.etrium.utils.EnergyTypes.ENERGY
import dev.deos.etrium.utils.EnergyTypes.MAX_ENERGY
import dev.deos.etrium.utils.EnergyTypes.REGEN
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity

object EnergyData {
    fun addEnergy(player: ServerPlayerEntity, amount: Float, type: String) {
        val nbt = (player as IEntityDataSaver).getPersistentData()
        when (type) {
            ENERGY -> {
                if (player.getEnergy() + amount > player.getMaxEnergy()) nbt.putFloat(ENERGY, player.getMaxEnergy())
                else nbt.putFloat(ENERGY, player.getEnergy() + amount)
            }

            MAX_ENERGY -> nbt.putFloat(MAX_ENERGY, player.getMaxEnergy() + amount)
            REGEN -> nbt.putFloat(REGEN, player.getRegen() + amount)
        }
    }

    fun removeEnergy(player: ServerPlayerEntity, amount: Float, type: String) {
        val nbt = (player as IEntityDataSaver).getPersistentData()
        when (type) {
            ENERGY -> {
                if (player.getEnergy() - amount < 0) nbt.putFloat(ENERGY, 0f)
                else nbt.putFloat(ENERGY, player.getEnergy() - amount)
            }

            MAX_ENERGY -> {
                if (player.getMaxEnergy() - amount < 0) {
                    nbt.putFloat(MAX_ENERGY, 0f)
                    setEnergy(player, 0f, ENERGY)
                } else {
                    if (player.getMaxEnergy() - amount < player.getEnergy()) setEnergy(
                        player,
                        player.getMaxEnergy() - amount,
                        ENERGY
                    )
                    nbt.putFloat(MAX_ENERGY, player.getMaxEnergy() - amount)
                }
            }

            REGEN -> {
                if (player.getRegen() - amount < 0) nbt.putFloat(REGEN, 0f)
                else nbt.putFloat(REGEN, player.getRegen() - amount)
            }
        }
    }

    fun setEnergy(player: ServerPlayerEntity, amount: Float, type: String) {
        val nbt = (player as IEntityDataSaver).getPersistentData()
        when (type) {
            ENERGY -> nbt.putFloat(ENERGY, amount)
            MAX_ENERGY -> {
                nbt.putFloat(MAX_ENERGY, amount)
                if (amount < player.getEnergy()) setEnergy(player, player.getMaxEnergy(), ENERGY)
            }

            REGEN -> nbt.putFloat(REGEN, amount)
        }
    }

    fun ServerPlayerEntity.getEnergy(): Float {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getFloat(ENERGY)
    }

    fun ServerPlayerEntity.getMaxEnergy(): Float {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getFloat(MAX_ENERGY)
    }

    fun ServerPlayerEntity.getRegen(): Float {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getFloat(REGEN)
    }
}

interface IEntityDataSaver {
    fun getPersistentData(): NbtCompound
}