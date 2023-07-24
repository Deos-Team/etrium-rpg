package dev.deos.etrium.mixin;

import dev.deos.etrium.event.PlayerTickEvent;
import dev.deos.etrium.utils.PlayerTickContainer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerTickMixin implements PlayerTickContainer {

    private int ticks = 0;

    @Inject(method = "playerTick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        this.ticks++;
        PlayerTickEvent.INSTANCE.getTICK().invoker().onTick((ServerPlayerEntity) (Object) this);
    }

    @Override
    public int getTicks() {
        return this.ticks;
    }
}
