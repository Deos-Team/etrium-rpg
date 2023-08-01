package dev.deos.etrium.utils

import dev.deos.etrium.EtriumClient
import dev.deos.etrium.api.InventoryTab
import dev.deos.etrium.api.Tab
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text


@Environment(EnvType.CLIENT)
object DrawTabHelper {
    fun drawTab(client: MinecraftClient?, context: DrawContext, screenClass: Screen, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        if (client?.player != null && screenClass is Tab) {
            var xPos = x - 24
            var shownTooltip: Text? = null
            val list: List<InventoryTab> = EtriumClient.invTabs
            for (i in list.indices) {
                val inventoryTab = list[i]
                if (inventoryTab.shouldShow(client)) {
                    var textureX = 24
                    context.drawTexture(EtriumClient.tabTexture, xPos, y, textureX, 0, 24, 21)
                    if (inventoryTab.texture != null) {
                        context.drawTexture(inventoryTab.texture, xPos + 5, y + 4, 0.0f, 0.0f, 14, 14, 14, 14)
                    } else if (inventoryTab.getItemStack(client) != null) {
                        context.drawItem(inventoryTab.getItemStack(client), xPos, y + 5)
                    }
                    if (isPointWithinBounds(x, y, xPos - x + 1, 5, 22, 19, mouseX.toDouble(), mouseY.toDouble())) {
                        shownTooltip = inventoryTab.title
                    }
                }
            }
            if (shownTooltip != null) {
                context.drawTooltip(client.textRenderer, shownTooltip, mouseX, mouseY)
            }
        }
    }

    fun onTabButtonClick(
        client: MinecraftClient?,
        screenClass: Screen,
        x: Int,
        y: Int,
        mouseX: Double,
        mouseY: Double,
        focused: Boolean
    ) {
        if (!focused && screenClass is Tab) {
            var xPos = x
            val list: List<InventoryTab> = EtriumClient.invTabs
            for (i in list.indices) {
                val inventoryTab = list[i]
                if (inventoryTab.shouldShow(client)) {
                    if (inventoryTab.canClick(screenClass.javaClass, client) && isPointWithinBounds(x, y, xPos - x + 1, 5, 22, 19, mouseX, mouseY)) {
                        inventoryTab.onClick(client)
                    }
                }
            }
        }
    }

    private fun isPointWithinBounds(
        xPos: Int,
        yPos: Int,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        pointX: Double,
        pointY: Double
    ): Boolean {
        var pointX = pointX
        var pointY = pointY
        return xPos.toDouble()
            .let { pointX -= it; pointX } >= (x - 1).toDouble() && pointX < (x + width + 1).toDouble() && yPos.toDouble()
            .let { pointY -= it; pointY } >= (y - 1).toDouble() && pointY < (y + height + 1).toDouble()
    }
}