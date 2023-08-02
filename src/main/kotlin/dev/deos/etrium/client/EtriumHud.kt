package dev.deos.etrium.client

import com.mojang.blaze3d.systems.RenderSystem
import dev.deos.etrium.Etrium
import dev.deos.etrium.utils.EnergyTypes.LEVEL
import dev.deos.etrium.utils.IEntityDataSaver
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.ColorHelper.Argb


class EtriumHud : HudRenderCallback {

    private val HUD = Identifier(Etrium.MI, "textures/hud/hud.png")
    private val client = MinecraftClient.getInstance()
    private var height = 0f
    private var width = 0f
    val yellow = takeColor(245, 203, 66)
    val black = takeColor(0, 0, 0)
    val white = takeColor(255, 255, 255)

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        if (client != null) {
            height = drawContext.scaledWindowHeight.toFloat()
            width = drawContext.scaledWindowWidth.toFloat()
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, HUD)

        if (!(client.player as PlayerEntity).isCreative && !(client.player as PlayerEntity).isSpectator) {
            drawContext.apply {
                renderHealthBar()
                renderHungerBar()
                renderOxygenBar()
                renderEnergyBar()
                renderArmor()
            }
        }

        drawContext.apply {
//            renderEntityLevel()
        }

    }

    private fun DrawContext.renderEntityLevel() {
        if (client.crosshairTarget == null) return
        if (client.crosshairTarget!!.type != HitResult.Type.ENTITY) return
        val entity = (client.crosshairTarget as EntityHitResult).entity
        val level = (entity as IEntityDataSaver).getPersistentData().getInt(LEVEL)
        RenderSystem.enableBlend()
        this.drawTextWithShadow(
            client.textRenderer, Text.literal(level.toString()).formatted(Formatting.BOLD),
            (width * 0.01f).toInt(), (height * 0.95f).toInt(), white
        )
        RenderSystem.disableBlend()
    }

    private fun DrawContext.renderHealthBar() {
        this.drawTexture(HUD, (width / 2 - 91).toInt(), (height - 40f).toInt(), 0, 0, 182, 14)
        val health = (client.player as PlayerEntity).health
        val maxHealth = (client.player as PlayerEntity).maxHealth
        if (health > 0) {
            val wth = if (maxHealth >= health) (182f * (health / maxHealth)).toInt() else 182
            this.drawTexture(
                HUD,
                (width / 2 - 91).toInt(),
                (height - 40f).toInt(),
                0,
                14,
                wth,
                14
            )
//          37f
            val wdth = health.toInt().toString().length * 6

            this.drawText(
                client.textRenderer, Text.literal("${health.toInt()}/${maxHealth.toInt()}"),
                (width / 2 - 3 - wdth).toInt(), (height - 37f).toInt(), white, true
            )
        }
    }

    private fun DrawContext.renderHungerBar() {
        this.drawTexture(HUD, (width / 2 - 91).toInt(), (height - 52).toInt(), 0, 28, 81, 8)
        val hunger = (client.player as PlayerEntity).hungerManager.foodLevel
        if (hunger > 0) {
            val wth = (81f * (hunger.toFloat() / 20f)).toInt()
            this.drawTexture(
                HUD,
                (width / 2 - 91).toInt(),
                (height - 52f).toInt(),
                0,
                36,
                wth,
                8
            )
        }
        this.drawTexture(HUD, (width / 2 - 96).toInt(), (height - 54).toInt(), 182, 0, 14, 14)
    }

    private fun DrawContext.renderOxygenBar() {
        val oxygen = (client.player as PlayerEntity).air
        val maxOxygen = (client.player as PlayerEntity).maxAir
        if (oxygen >= maxOxygen) return
        this.drawTexture(HUD, (width / 2 + 10).toInt(), (height - 52).toInt(), 81, 28, 81, 8)
        val wth = (81f * (oxygen.toFloat() / maxOxygen)).toInt()
        this.drawTexture(
            HUD,
            (width / 2 + 10).toInt(),
            (height - 52f).toInt(),
            81,
            36,
            wth,
            8
        )
        this.drawTexture(HUD, (width / 2 + 3).toInt(), (height - 55).toInt(), 196, 0, 14, 15)
    }

    private fun DrawContext.renderArmor() {
        this.drawTexture(HUD, (width / 2 - 120f).toInt(), (height - 36f).toInt(), 182, 15, 22, 31)
        val armor = (client.player as PlayerEntity).armor
        if (armor > 0) {
            val wdth = armor.toString().length * 6
            this.drawText(
                client.textRenderer, Text.literal(armor.toString()),
                (width / 2 - 125f - wdth).toInt(), (height - 25f).toInt(), white, true
            )
        }
    }

    private fun DrawContext.renderEnergyBar() {
        this.drawTexture(HUD, (width / 2 + 100).toInt(), (height - 36f).toInt(), 210, 0, 17, 31)
        val energy = (client.player as IEntityDataSaver).getPersistentData().getFloat("energy")
        val maxEnergy = (client.player as IEntityDataSaver).getPersistentData().getFloat("maxEnergy")
        val hgt = if (maxEnergy >= energy) (31f * (1f - (energy / maxEnergy))).toInt() else 0
        this.drawTexture(
            HUD,
            (width / 2 + 100).toInt(),
            (height - 36f).toInt(),
            227,
            0,
            20,
            hgt
        )
        this.drawText(
            client.textRenderer, Text.literal("/"),
            (width / 2 + 124f).toInt(), (height - 25f).toInt(), white, true
        )
        this.drawText(
            client.textRenderer, Text.literal(energy.toInt().toString()),
            (width / 2 + 124f).toInt(), (height - 36f).toInt(), white, true
        )
        this.drawText(
            client.textRenderer, Text.literal(maxEnergy.toInt().toString()),
            (width / 2 + 124f).toInt(), (height - 15f).toInt(), white, true
        )
    }

    private fun takeColor(r: Int, g: Int, b: Int, invis: Int = 255): Int {
        return Argb.getArgb(invis, r, g, b)
    }

}