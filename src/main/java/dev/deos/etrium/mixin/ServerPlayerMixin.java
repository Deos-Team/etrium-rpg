package dev.deos.etrium.mixin;

import dev.deos.etrium.event.PlayerJoinEvent;
import dev.deos.etrium.event.PlayerTickEvent;
import dev.deos.etrium.utils.PlayerTickContainer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
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

    @Inject(method = "onSpawn", at = @At("TAIL"))
    public void onJoin(CallbackInfo ci) {
        if (this.statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) >= 1) return;
        PlayerJoinEvent.INSTANCE.getJOIN().invoker().onJoin((ServerPlayerEntity) (Object) this);
    }

    @Override
    public int getTick() {
        return tick;
    }

}
