package dev.deos.etrium.mixin;

import dev.deos.etrium.utils.IEntityDataSaver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements IEntityDataSaver {

    @Unique
    private NbtCompound persistentData;

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
        if (persistentData != null) {
            nbt.put("etrium.data", persistentData);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("etrium.data", 10)) {
            persistentData = nbt.getCompound("etrium.data");
        }
    }


}
