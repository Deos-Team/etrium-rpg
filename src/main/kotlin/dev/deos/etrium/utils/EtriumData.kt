package dev.deos.etrium.utils

import dev.deos.etrium.utils.EnergyTypes.ENERGY
import dev.deos.etrium.utils.EnergyTypes.LEVEL
import dev.deos.etrium.utils.EnergyTypes.MAX_ENERGY
import dev.deos.etrium.utils.EnergyTypes.REGEN
import dev.deos.etrium.utils.EnergyTypes.XP
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import kotlin.random.Random

object EtriumData {
    fun addEnergy(entity: LivingEntity, amount: Float, type: String) {
        val nbt = (entity as IEntityDataSaver).getPersistentData()
        when (type) {
            ENERGY -> {
                if (entity.getEnergy() + amount > entity.getMaxEnergy()) nbt.putFloat(ENERGY, entity.getMaxEnergy())
                else nbt.putFloat(ENERGY, entity.getEnergy() + amount)
            }

            MAX_ENERGY -> nbt.putFloat(MAX_ENERGY, entity.getMaxEnergy() + amount)
            REGEN -> nbt.putFloat(REGEN, entity.getRegen() + amount)
        }
    }

    fun addLevel(entity: LivingEntity, amount: Int, type: String) {
        val nbt = (entity as IEntityDataSaver).getPersistentData()
        when (type) {
            LEVEL -> {
                if (entity.getLevel() + amount > LevelAmount.MAX_LEVEL) nbt.putInt(LEVEL, LevelAmount.MAX_LEVEL)
                else nbt.putInt(LEVEL, entity.getLevel() + amount)
            }

            XP -> {
                if (entity.getLevel() >= LevelAmount.MAX_LEVEL) return
                else if ((entity.getXp() + amount) >= (LevelAmount.levelList[entity.getLevel()] ?: 0)) {
                    val new = amount - LevelAmount.levelList[entity.getLevel()]?.minus(entity.getXp())!!
                    nbt.putInt(LEVEL, entity.getLevel() + 1)
                    nbt.putInt(XP, 0)
                    addLevel(entity, new, XP)
                } else nbt.putInt(XP, entity.getXp() + amount)
            }
        }
    }

    fun removeEnergy(entity: LivingEntity, amount: Float, type: String) {
        val nbt = (entity as IEntityDataSaver).getPersistentData()
        when (type) {
            ENERGY -> {
                if (entity.getEnergy() - amount < 0) nbt.putFloat(ENERGY, 0f)
                else nbt.putFloat(ENERGY, entity.getEnergy() - amount)
            }

            MAX_ENERGY -> {
                if (entity.getMaxEnergy() - amount < 0) {
                    nbt.putFloat(MAX_ENERGY, 0f)
                    setEnergy(entity, 0f, ENERGY)
                } else {
                    if (entity.getMaxEnergy() - amount < entity.getEnergy()) setEnergy(
                        entity,
                        entity.getMaxEnergy() - amount,
                        ENERGY
                    )
                    nbt.putFloat(MAX_ENERGY, entity.getMaxEnergy() - amount)
                }
            }

            REGEN -> {
                if (entity.getRegen() - amount < 0) nbt.putFloat(REGEN, 0f)
                else nbt.putFloat(REGEN, entity.getRegen() - amount)
            }
        }
    }

    fun removeLevel(entity: LivingEntity, amount: Int, type: String) {
        val nbt = (entity as IEntityDataSaver).getPersistentData()
        when (type) {
            LEVEL -> {
                if (entity.getLevel() - amount < 0) nbt.putInt(LEVEL, 0)
                else nbt.putInt(LEVEL, entity.getLevel() - amount)
            }

            XP -> {
                if (entity.getLevel() == 1 && entity.getXp() - amount <= 0) nbt.putInt(XP, 0)
                else if (entity.getXp() - amount == 0) nbt.putInt(XP, 0)
                else if (entity.getXp() - amount < 0) {
                    val new = amount - entity.getXp()
                    nbt.putInt(XP, LevelAmount.levelList[entity.getLevel() - 1] ?: 0)
                    nbt.putInt(LEVEL, entity.getLevel() - 1)
                    removeLevel(entity, new, XP)
                } else nbt.putInt(XP, entity.getXp() - amount)
            }
        }
    }

    fun setEnergy(entity: LivingEntity, amount: Float, type: String) {
        val nbt = (entity as IEntityDataSaver).getPersistentData()
        when (type) {
            ENERGY -> {
                nbt.putFloat(ENERGY, amount)
            }

            MAX_ENERGY -> {
                nbt.putFloat(MAX_ENERGY, amount)
                if (amount < entity.getEnergy()) setEnergy(entity, entity.getMaxEnergy(), ENERGY)
            }

            REGEN -> nbt.putFloat(REGEN, amount)
        }
    }

    fun setLevel(entity: LivingEntity, amount: Int, type: String) {
        val nbt = (entity as IEntityDataSaver).getPersistentData()
        when (type) {
            LEVEL -> nbt.putInt(LEVEL, amount)
            XP -> nbt.putInt(XP, amount)
        }
    }

    fun LivingEntity.getEnergy(): Float {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getFloat(ENERGY)
    }

    fun LivingEntity.getMaxEnergy(): Float {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getFloat(MAX_ENERGY)
    }

    fun LivingEntity.getRegen(): Float {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getFloat(REGEN)
    }

    fun LivingEntity.getLevel(): Int {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getInt(LEVEL)
    }

    fun LivingEntity.getXp(): Int {
        val nbt = (this as IEntityDataSaver).getPersistentData()
        return nbt.getInt(XP)
    }

}

interface IEntityDataSaver {
    fun getPersistentData(): NbtCompound
}
