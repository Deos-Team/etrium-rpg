package dev.deos.etrium.mixin;

import dev.deos.etrium.event.EntityKillEvent;
import dev.deos.etrium.event.PlayerTickEvent;
import dev.deos.etrium.utils.PlayerTickContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin implements PlayerTickContainer {

    @Final
    private ServerStatHandler statHandler;
    private int tick;

    @Inject(method = "playerTick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        tick++;
        PlayerTickEvent.INSTANCE.getTICK().invoker().onTick((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "updateKilledAdvancementCriterion", at = @At("TAIL"))
    protected void onKill(Entity entityKilled, int score, DamageSource damageSource, CallbackInfo ci) {
        if (entityKilled.isPlayer()) return;
        EntityKillEvent.INSTANCE.getKill().invoker().onKill((ServerPlayerEntity) (Object) this, entityKilled);
    }

    @Override
    public int getTick() {
        return tick;
    }

}
