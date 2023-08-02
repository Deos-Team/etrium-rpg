package dev.deos.etrium.mixin;

import dev.deos.etrium.event.SpawnEntityEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "spawnEntity", at = @At("HEAD"))
    private void onSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof MobEntity) {
            SpawnEntityEvent.INSTANCE.getSPAWN().invoker().onSpawn((MobEntity) entity);
        }
    }
    /*@Inject(method = "spawnNewEntityAndPassengers", at = @At("HEAD"))
    private void onSpawns(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if (entity instanceof MobEntity) {
            SpawnEntityEvent.INSTANCE.getSPAWN().invoker().onSpawn((MobEntity) entity);
        }
    }*/
}
