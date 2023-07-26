package dev.deos.etrium.client

import com.mojang.blaze3d.systems.RenderSystem
import dev.deos.etrium.Etrium
import dev.deos.etrium.utils.IEntityDataSaver
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper.Argb

class EnergyHud : HudRenderCallback {

    private val FILLED_ENERGY = Identifier(Etrium.MI, "textures/energy/filled_energy.png")
    private val HUD = Identifier(Etrium.MI, "textures/hud/hud.png")
    private val client = MinecraftClient.getInstance()
    private var height = 0f
    private var width = 0f

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        if (client != null) {
            height = drawContext.scaledWindowHeight.toFloat()
            width = drawContext.scaledWindowWidth.toFloat()
        }
        val yellow = takeColor(245, 203, 66)
        val black = takeColor(0, 0, 0)
        val white = takeColor(255, 255, 255)
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, FILLED_ENERGY)

        /*drawContext.fill(
            RenderLayer.getGuiOverlay(),
            (width / 1.005).toInt(),
            (height / 1.02).toInt(),
            (width / 1.065).toInt(),
            (height / 1.40).toInt(),
            yellow
        )*/
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
            (width / 1.045).toInt(), (height / 1.20).toInt(), white, false
        )

        renderHealthBar(drawContext)

    }

    private fun renderHealthBar(drawContext: DrawContext) {
        drawContext.drawTexture(HUD, (width / 1.5).toInt(), (height / 1.5).toInt(), 0, 0, 80, 8)
        val health = (client.player as IEntityDataSaver).getPersistentData().getInt("health")
        val maxHealth = (client.player as IEntityDataSaver).getPersistentData().getInt("maxHealth")
        if (health > 0) {
            drawContext.drawTexture(
                HUD,
                (width / 1.5).toInt(),
                (height / 1.5).toInt(),
                0,
                8,
                (80f * (health.toFloat() / maxHealth.toFloat())).toInt(),
                8
            )
        }
    }

    private fun takeColor(r: Int, g: Int, b: Int, invis: Int = 255): Int {
        return Argb.getArgb(invis, r, g, b)
    }

}