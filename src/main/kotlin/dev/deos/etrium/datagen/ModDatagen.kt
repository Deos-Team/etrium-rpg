package dev.deos.etrium.datagen

import dev.deos.etrium.world.biome.BiomeRegistry
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryKeys

object ModDatagen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {

        val pack = fabricDataGenerator.createPack()

        pack.addProvider(::ModWorldGen)

    }

    override fun buildRegistry(registryBuilder: RegistryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.BIOME, BiomeRegistry::bootstrap)
//        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, MnConfiguredFeatures::bootstrap)
//        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, MnPlacedFeatures::bootstrap)
    }
}