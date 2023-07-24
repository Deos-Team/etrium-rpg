package dev.deos.etrium.mixin;

import dev.deos.etrium.event.PlayerJoinEvent;
import dev.deos.etrium.event.PlayerTickEvent;
import dev.deos.etrium.utils.IEntityDataSaver;
import dev.deos.etrium.utils.PlayerTickContainer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin implements IEntityDataSaver, PlayerTickContainer {
    @Shadow
    @Final
    private ServerStatHandler statHandler;
    private NbtCompound persistentData;
    private int tick;

    @NotNull
    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            return persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfo ci) {
        if(persistentData != null) {
            nbt.put("etrium.data", persistentData);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("etrium.data", 10)) {
            persistentData = nbt.getCompound("etrium.data");
        }
    }

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
