package dev.deos.etrium.network

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

object EnergySyncDataC2SPacket {
    fun receive(
        server: MinecraftServer, player: ServerPlayerEntity,
        handler: ServerPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender
    ) {

    }
}