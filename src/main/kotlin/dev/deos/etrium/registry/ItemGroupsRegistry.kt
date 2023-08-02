package dev.deos.etrium.registry

import dev.deos.etrium.Etrium
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier


object ItemGroupsRegistry {

    val ITEM_GROUP = register("item_group", "Test Group", ItemRegistry.ModItems.TEST_SWORD.item)

    init {
        ItemRegistry.ModItems.values().forEach { here ->
            ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register {
                it.add(ItemStack(here.item))
            }
        }
    }

    private fun register(id: String, name: String, icon: Item): RegistryKey<ItemGroup> {
        val group: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier(Etrium.MI, id))
        Registry.register(Registries.ITEM_GROUP, group,
            FabricItemGroup.builder()
                .icon { ItemStack(icon) }
                .displayName(Text.literal(name))
                .build()
        )
        return group
    }

}