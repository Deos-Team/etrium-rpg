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

    private float regen;

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

    public float getRegen() {
        return regen;
    }

    public void setRegen(float regen) {
        this.regen = regen;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteEntityToNBT(NbtCompound compound, CallbackInfo ci) {
        compound.putFloat("maxEnergy", this.getMaxEnergy());
        compound.putFloat("energy", this.getEnergy());
        compound.putFloat("regen", this.getRegen());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadEntityFromNBT(NbtCompound compound, CallbackInfo ci) {
        setEnergy(compound.getFloat("maxEnergy"));
        setEnergy(compound.getFloat("energy"));
        setRegen(compound.getFloat("regen"));
    }

}
