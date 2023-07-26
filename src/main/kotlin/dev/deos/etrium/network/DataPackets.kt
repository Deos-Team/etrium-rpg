package dev.deos.etrium.network

import dev.deos.etrium.Etrium
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.util.Identifier

object DataPackets {

    val ENERGY_ID = Identifier(Etrium.MI, "energy_sync")
    val HEALTH_ID = Identifier(Etrium.MI, "health_sync")
    val MAX_HEALTH_ID = Identifier(Etrium.MI, "max_health_sync")

    fun registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_ID, ModSyncDataS2CPacket::energyReceive)
        ClientPlayNetworking.registerGlobalReceiver(HEALTH_ID, ModSyncDataS2CPacket::healthReceive)
        ClientPlayNetworking.registerGlobalReceiver(MAX_HEALTH_ID, ModSyncDataS2CPacket::maxHealthReceive)
    }
}