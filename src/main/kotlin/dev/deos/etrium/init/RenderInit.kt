package dev.deos.etrium.init

import dev.deos.etrium.Etrium.MI
import dev.deos.etrium.client.screen.SkillsScreen
import dev.deos.etrium.client.screen.widget.SkillsTab
import dev.deos.etrium.utils.TabReg
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object RenderInit {
    init {
        //TabReg.registerInventoryTab(VanillaInventoryTab(Text.literal("Inv"), Identifier("sdf"), 0, InventoryScreen::class.java))
        TabReg.registerInventoryTab(SkillsTab(Text.literal("Skills"), Identifier(MI, "gui/skills_icon"), 1, SkillsScreen::class.java))
    }
}