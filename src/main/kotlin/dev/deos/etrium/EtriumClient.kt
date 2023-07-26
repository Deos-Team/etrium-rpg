package dev.deos.etrium

import dev.deos.etrium.client.EnergyHud
import dev.deos.etrium.init.RenderInit
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.libz.api.InventoryTab

object EtriumClient : ClientModInitializer {
    val invTabs: ArrayList<InventoryTab> = ArrayList()
    override fun onInitializeClient() {
        RenderInit
        HudRenderCallback.EVENT.register(EnergyHud())
    }
}