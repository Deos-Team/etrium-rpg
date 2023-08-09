package dev.deos.etrium

import dev.deos.etrium.client.EtriumHud
import dev.deos.etrium.network.DataPackets
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object EtriumClient : ClientModInitializer {
    override fun onInitializeClient() {
        DataPackets.registerS2CPackets()
        HudRenderCallback.EVENT.register(EtriumHud())
    }
}