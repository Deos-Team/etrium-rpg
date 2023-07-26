package dev.deos.etrium

import dev.deos.etrium.client.EtriumHud
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object EtriumClient : ClientModInitializer {
    override fun onInitializeClient() {
        HudRenderCallback.EVENT.register(EtriumHud())
    }
}