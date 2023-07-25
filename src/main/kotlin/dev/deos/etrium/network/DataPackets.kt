package dev.deos.etrium.network

import dev.deos.etrium.Etrium
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.util.Identifier

object DataPackets {

    val ENERGY_ID = Identifier(Etrium.MI, "energy_sync")

    fun registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_ID, EnergySyncDataS2CPacket::receive)
    }
}