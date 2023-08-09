package dev.deos.etrium

import dev.deos.etrium.api.InventoryTab
import dev.deos.etrium.client.EtriumHud
import dev.deos.etrium.client.screen.SkillsScreen
import dev.deos.etrium.client.screen.widget.SkillsTab
import dev.deos.etrium.client.utils.TabReg
import dev.deos.etrium.network.DataPackets
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object EtriumClient : ClientModInitializer {

    val invTabs: ArrayList<InventoryTab> = ArrayList()
    val tabTexture: Identifier = Identifier("etrium:textures/gui/icons.png")

    override fun onInitializeClient() {

        onInit()
        DataPackets.registerS2CPackets()
        HudRenderCallback.EVENT.register(EtriumHud())

    }

    private fun onInit() {
        TabReg.registerInventoryTab(
            SkillsTab(
                Text.literal("Skills"),
                tabTexture, 1, SkillsScreen::class.java
            )
        )
    }

}