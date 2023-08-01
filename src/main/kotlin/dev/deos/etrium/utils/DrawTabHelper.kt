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
            var xPos = x
            var shownTooltip: Text? = null
            val list: List<InventoryTab> = EtriumClient.invTabs
            for (i in list.indices) {
                val inventoryTab = list[i]
                if (inventoryTab.shouldShow(client)) {
                    val isFirstTab = i == 0
                    val isSelectedTab = inventoryTab.isSelectedScreen(screenClass.javaClass)
                    var textureX = if (isFirstTab) 24 else 72
                    if (isSelectedTab) {
                        textureX -= 24
                    }
                    context.drawTexture(
                        EtriumClient.tabTexture,
                        xPos,
                        if (isSelectedTab) y - 23 else y - 21,
                        textureX,
                        0,
                        24,
                        if (isSelectedTab) 27 else if (isFirstTab) 25 else 21
                    )
                    if (inventoryTab.texture != null) {
                        context.drawTexture(inventoryTab.texture, xPos + 5, y - 16, 0.0f, 0.0f, 14, 14, 14, 14)
                    } else if (inventoryTab.getItemStack(client) != null) {
                        context.drawItem(inventoryTab.getItemStack(client), xPos + 4, y - 17)
                    }
                    if (!isSelectedTab && isPointWithinBounds(
                            x,
                            y,
                            xPos - x + 1,
                            -20,
                            22,
                            19,
                            mouseX.toDouble(),
                            mouseY.toDouble()
                        )
                    ) {
                        shownTooltip = inventoryTab.title
                    }
                    xPos += 25
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
                    val isSelectedTab = inventoryTab.isSelectedScreen(screenClass.javaClass)
                    if (inventoryTab.canClick(screenClass.javaClass, client) && isPointWithinBounds(
                            x,
                            y,
                            xPos - x + 1,
                            if (isSelectedTab) -24 else -20,
                            22,
                            if (isSelectedTab) 23 else 19,
                            mouseX,
                            mouseY
                        )
                    ) {
                        inventoryTab.onClick(client)
                    }
                    xPos += 25
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