package dev.deos.etrium.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class HudPlayerMixin {
    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    protected void disableRenderExp(DrawContext context, int x, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    protected void disableMountHealth(DrawContext context, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    protected void disableStatusBars(DrawContext context, CallbackInfo ci) {
        ci.cancel();
    }
}
