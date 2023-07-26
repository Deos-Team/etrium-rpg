package dev.deos.etrium.client.screen.widget

import dev.deos.etrium.client.screen.SkillsScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.libz.api.InventoryTab
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class SkillsTab(title: Text?, texture: Identifier?, preferedPos: Int, screenClass: Class<*>) : InventoryTab(title, texture, preferedPos, screenClass) {
    override fun canClick(screenClass: Class<*>?, client: MinecraftClient?): Boolean {
        return if (screenClass!! == SkillsScreen::class.java) {
            true
        } else super.canClick(screenClass, client)
    }

    override fun onClick(client: MinecraftClient?) {
        client?.setScreen(SkillsScreen())
    }
}