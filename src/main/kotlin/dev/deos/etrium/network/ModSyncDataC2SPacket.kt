package dev.deos.etrium.network

import dev.deos.etrium.utils.EtriumData.getLevel
import dev.deos.etrium.utils.IEntityDataSaver
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.entity.LivingEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

object ModSyncDataC2SPacket {
    fun levelEntityReceive(
        server: MinecraftServer, player: ServerPlayerEntity,
        handler: ServerPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender
    ) {
        val entity = player.world.getEntityById(buf.readInt()) as LivingEntity
        val level = entity.getLevel()
        (player as IEntityDataSaver).getPersistentData().putInt("entityLevel", level)
    }
}