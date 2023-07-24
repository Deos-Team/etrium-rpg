package dev.deos.etrium.mixin;

import dev.deos.etrium.utils.EnergyContainer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerEntityMixin implements EnergyContainer {
    private float energy = 0F;
    private float maxEnergy = 100F;
    private float regenEnergy = 1.0F;

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

    @Override
    public float getRegenEnergy() {
        return this.regenEnergy;
    }

    @Override
    public void setRegenEnergy(float value) {
        this.regenEnergy = value;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteEntityToNBT(NbtCompound compound, CallbackInfo ci) {
        compound.putFloat("energy", this.getEnergy());
        compound.putFloat("maxEnergy", this.getMaxEnergy());
        compound.putFloat("regenEnergy", this.getRegenEnergy());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadEntityFromNBT(NbtCompound compound, CallbackInfo ci) {
        setEnergy(compound.getFloat("energy"));
        setMaxEnergy(compound.getFloat("maxEnergy"));
        setRegenEnergy(compound.getFloat("regenEnergy"));
    }
}
