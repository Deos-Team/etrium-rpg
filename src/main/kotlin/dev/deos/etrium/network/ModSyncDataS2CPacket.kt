package dev.deos.etrium.network

import dev.deos.etrium.utils.IEntityDataSaver
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

object ModSyncDataS2CPacket {
    fun energyReceive(
        client: MinecraftClient, handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf, responseSender: PacketSender
    ) {
        (client.player as IEntityDataSaver).getPersistentData().putFloat("energy", buf.readFloat())
    }

    fun maxEnergyReceive(
        client: MinecraftClient, handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf, responseSender: PacketSender
    ) {
        (client.player as IEntityDataSaver).getPersistentData().putFloat("maxEnergy", buf.readFloat())
    }

    fun levelRecieve(
        client: MinecraftClient, handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf, responseSender: PacketSender
    ) {
        (client.player as IEntityDataSaver).getPersistentData().putInt("level", buf.readInt())
    }

}