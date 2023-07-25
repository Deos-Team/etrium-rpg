package dev.deos.etrium.network

import dev.deos.etrium.utils.IEntityDataSaver
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

object EnergySyncDataS2CPacket {
    fun receive(
        client: MinecraftClient, handler: ClientPlayNetworkHandler,
        buf: PacketByteBuf, responseSender: PacketSender
    ) {
        (client.player as IEntityDataSaver).getPersistentData().putFloat("energy", buf.readFloat())
    }
}