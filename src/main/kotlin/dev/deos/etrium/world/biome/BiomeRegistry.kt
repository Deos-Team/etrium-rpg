package dev.deos.etrium.world.biome

import dev.deos.etrium.Etrium
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BiomeAdditionsSound
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import net.minecraft.world.biome.*

object BiomeRegistry {

    val black = takeColor(0, 0, 0, 255)
    val white = takeColor(255, 255, 255, 255)

    fun bootstrap(biomeRegisterable: Registerable<Biome>) {
        biomeRegisterable.apply {
            register(ModBiomes.DEAD_FOREST.key(), ModBiomes.DEAD_FOREST.biome)
        }
    }

    enum class ModBiomes(
        val value: String,
        val biome: Biome
    ) {
        DEAD_FOREST("dead_forest", deadForest)
    }

    fun ModBiomes.key(): RegistryKey<Biome> {
        return RegistryKey.of(RegistryKeys.BIOME, Identifier(Etrium.MI, this.value))
    }

    private fun takeColor(r: Int, g: Int, b: Int, invis: Int = 255): Int {
        return ColorHelper.Argb.getArgb(invis, r, g, b)
    }

    val deadForest = Biome.Builder()
        .temperature(1.0f)
        .downfall(1.0f)
        .precipitation(true)
        .effects(
            BiomeEffects.Builder()
                .fogColor(black)
                .grassColor(white)
                .foliageColor(white)
                .additionsSound(BiomeAdditionsSound(SoundEvents.AMBIENT_CAVE, 1.0))
                .skyColor(white)
                .waterColor(white)
                .waterFogColor(white)
                .build()
        )
        .spawnSettings(
            SpawnSettings.Builder()
                .spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 10, 1, 4))
                .build()
        )
        .generationSettings(
            GenerationSettings.Builder()
                .build()
        )
        .build()

}