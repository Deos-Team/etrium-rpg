package dev.deos.etrium

import dev.deos.etrium.api.InventoryTab
import dev.deos.etrium.client.EnergyHud
import dev.deos.etrium.init.RenderInit
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.util.Identifier

object EtriumClient : ClientModInitializer {
    val invTabs: ArrayList<InventoryTab> = ArrayList()
    val tabTexture: Identifier = Identifier("etrium:textures/gui/icons.png")
    override fun onInitializeClient() {
        RenderInit
        HudRenderCallback.EVENT.register(EnergyHud())
    }
}