package dev.deos.etrium.mixin;

import dev.deos.etrium.utils.EnergyContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements EnergyContainer {
    private float energy;
    private float maxEnergy = 100F;

    @Override
    public float getEnergy() {
        return this.energy;
    }

    @Override
    public void setEnergy(float value) {
        this.energy = value;
    }

    @Override
    public float getMaxEnergy() {
        return this.maxEnergy;
    }

    @Override
    public void setMaxEnergy(float value) {
        this.maxEnergy = value;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteEntityToNBT(NbtCompound compound, CallbackInfo ci) {
        compound.putFloat("energy", this.getEnergy());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadEntityFromNBT(NbtCompound compound, CallbackInfo ci) {
        setEnergy(compound.getFloat("energy"));
    }
}
