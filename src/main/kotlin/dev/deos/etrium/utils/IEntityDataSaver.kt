package dev.deos.etrium.utils

import net.minecraft.nbt.NbtCompound

interface IEntityDataSaver {
    fun getPersistentData(): NbtCompound
}