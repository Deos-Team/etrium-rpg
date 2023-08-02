package dev.deos.etrium.registry

import dev.deos.etrium.Etrium
import dev.deos.etrium.item.BiomeEditor
import dev.deos.etrium.item.EtriumSwordItem
import dev.deos.etrium.item.LevelGetter
import dev.deos.etrium.item.WeaponToolMaterials
import dev.deos.etrium.utils.ModStatHandler
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterial
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.shape.VoxelShapes

object ItemRegistry {

    init {
        ModItems.values().forEach {
            it.register()
        }
    }

    enum class ModItems(val id: String, val item: Item) {
        TEST_SWORD("test_sword", Item(FabricItemSettings().maxDamage(100))),
        OBTANIUM_SWORD(
            "obtanium_sword", EtriumSwordItem(
                WeaponToolMaterials.OBTANIUM,
                12, 1.0f, FabricItemSettings()
            )
        ),
        LEVEL_GETTER("level_getter", LevelGetter(FabricItemSettings().maxCount(1))),
        BIOME_EDITOR("biome_editor", BiomeEditor(FabricItemSettings().maxCount(1)))
    }

    private fun ModItems.register() {
        Registry.register(Registries.ITEM, Identifier(Etrium.MI, this.id), this.item)
    }

}

