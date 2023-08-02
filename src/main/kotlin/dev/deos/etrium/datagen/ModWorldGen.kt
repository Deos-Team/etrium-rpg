package dev.deos.etrium.datagen

import dev.deos.etrium.Etrium
import dev.deos.etrium.world.biome.BiomeRegistry
import dev.deos.etrium.world.biome.BiomeRegistry.key
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.biome.Biome
import java.util.concurrent.CompletableFuture

class ModWorldGen(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricDynamicRegistryProvider(output, registriesFuture) {

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.BIOME))
    }

    override fun getName(): String {
        return Etrium.MI
    }

}