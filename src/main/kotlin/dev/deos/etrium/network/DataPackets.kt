package dev.deos.etrium.network

import dev.deos.etrium.Etrium
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier

object DataPackets {

    val ENERGY_ID = Identifier(Etrium.MI, "energy_sync")
    val MAX_ENERGY_ID = Identifier(Etrium.MI, "max_energy_sync")
    val LEVEL_ID = Identifier(Etrium.MI, "level_sync")
    val SERVER_ENTITY_ID = Identifier(Etrium.MI, "server_entity_sync")

    val CLIENT_ENTITY_ID = Identifier(Etrium.MI, "client_entity_sync")

    fun registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_ID, ModSyncDataS2CPacket::energyReceive)
        ClientPlayNetworking.registerGlobalReceiver(MAX_ENERGY_ID, ModSyncDataS2CPacket::maxEnergyReceive)
        ClientPlayNetworking.registerGlobalReceiver(LEVEL_ID, ModSyncDataS2CPacket::levelReceive)
        ClientPlayNetworking.registerGlobalReceiver(SERVER_ENTITY_ID, ModSyncDataS2CPacket::levelEntityRecieve)
    }

    fun registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_ENTITY_ID, ModSyncDataC2SPacket::levelEntityReceive)
    }

}