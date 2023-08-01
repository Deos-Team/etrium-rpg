package dev.deos.etrium.client.screen.widget

import dev.deos.etrium.api.InventoryTab
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class VanillaInventoryTab(title: Text, texture: Identifier?, preferedPos: Int, screenClass: Class<*>) : InventoryTab(title, texture, preferedPos, screenClass) {
    override fun onClick(client: MinecraftClient?) {
        client?.setScreen(InventoryScreen(client.player))
    }
}