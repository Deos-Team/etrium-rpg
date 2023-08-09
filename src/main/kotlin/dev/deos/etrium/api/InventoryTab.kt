package dev.deos.etrium.api

import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

open class InventoryTab(title: Text, texture: Identifier?, preferredPose: Int, vararg screenClasses: Class<*>) {
    private val screenClasses: Array<Class<*>>
    val title: Text
    val texture: Identifier?
    val preferedPos: Int

    init {
        this.screenClasses = screenClasses as Array<Class<*>>
        this.title = title
        this.texture = texture
        this.preferedPos = preferredPose
    }

    fun getItemStack(client: MinecraftClient?): ItemStack? {
        return null
    }

    fun shouldShow(client: MinecraftClient?): Boolean {
        return true
    }

    open fun onClick(client: MinecraftClient?) {

    }

    open fun canClick(screenClass: Class<*>, client: MinecraftClient?): Boolean {
        return !isSelectedScreen(screenClass)
    }

    private fun isSelectedScreen(screenClass: Class<*>): Boolean {
        for (i in screenClasses.indices) {
            if (screenClasses[i] == screenClass) {
                return true
            }
        }
        return false
    }
}