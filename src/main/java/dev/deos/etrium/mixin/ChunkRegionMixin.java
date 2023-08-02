package dev.deos.etrium.mixin;

import dev.deos.etrium.event.SpawnEntityEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRegion.class)
public class ChunkRegionMixin {
    @Shadow
    @Final
    private BiomeAccess biomeAccess;

    @Inject(method = "spawnEntity", at = @At("HEAD"))
    private void onSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof MobEntity) {
            RegistryEntry<Biome> biome = this.biomeAccess.getBiome(entity.getBlockPos());
            SpawnEntityEvent.INSTANCE.getSPAWN().invoker().onSpawn((MobEntity) entity);
        }
    }
}
