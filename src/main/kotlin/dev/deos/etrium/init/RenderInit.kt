package dev.deos.etrium.init

import dev.deos.etrium.client.screen.SkillsScreen
import dev.deos.etrium.client.screen.widget.SkillsTab
import dev.deos.etrium.client.screen.widget.VanillaInventoryTab
import net.libz.registry.TabRegistry
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object RenderInit {
    init {
        TabRegistry.registerInventoryTab(VanillaInventoryTab(Text.literal("Inv"), Identifier("sdf"), 0, InventoryScreen::class.java))
        TabRegistry.registerInventoryTab(SkillsTab(Text.literal("Test"), Identifier("dasd"), 1, SkillsScreen::class.java))
    }
}