package dev.deos.etrium.client

import com.mojang.blaze3d.systems.RenderSystem
import dev.deos.etrium.Etrium
import dev.deos.etrium.utils.IEntityDataSaver
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.RenderLayer
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper.Argb

class EnergyHud : HudRenderCallback {

    private val FILLED_ENERGY = Identifier(Etrium.MI, "textures/energy/filled_energy.png")

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        var x = 0f
        var y = 0f
        val client = MinecraftClient.getInstance()
        if (client != null) {
            val width = client.window.scaledWidth.toFloat()
            val height = client.window.scaledHeight.toFloat()
            x = width
            y = height
        }
        val yellow = takeColor(245, 203, 66)
        val black = takeColor(0, 0, 0)
        val white = takeColor(255, 255, 255)
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, FILLED_ENERGY)

        drawContext.fill(
            RenderLayer.getGuiOverlay(),
            (x / 1.005).toInt(),
            (y / 1.02).toInt(),
            (x / 1.065).toInt(),
            (y / 1.40).toInt(),
            yellow
        )
        /*drawContext.fill(
            RenderLayer.getGuiOverlay(),
            (x / 1.23).toInt(),
            (y / 1.46).toInt(),
            (x / 1.40).toInt(),
            (y / 1.70).toInt(),
            black
        )*/

        val energy = (client.player as IEntityDataSaver).getPersistentData().getFloat("energy")
        drawContext.drawText(
            client.textRenderer, Text.literal(energy.toInt().toString()).formatted(Formatting.BOLD),
            (x / 1.045).toInt(), (y / 1.20).toInt(), white, false
        )

    }

    private fun takeColor(r: Int, g: Int, b: Int, invis: Int = 255): Int {
        return Argb.getArgb(invis, r, g, b)
    }

}