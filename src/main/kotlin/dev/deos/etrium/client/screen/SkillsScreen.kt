package dev.deos.etrium.client.screen

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.libz.api.Tab
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class SkillsScreen() : Screen(Text.literal("Skills")), Tab {
    private val backgroundWidth = 200
    private val backgroundHeight = 215
    override fun init() {
        super.init()
    }

    override fun shouldPause(): Boolean = false

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(context)
        super.render(context, mouseX, mouseY, delta)
    }
}