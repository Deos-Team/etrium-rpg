package dev.deos.etrium.mixin;

import dev.deos.etrium.client.utils.DrawTabHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    public HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        DrawTabHelper.INSTANCE.drawTab(client, context, this, x, y, mouseX, mouseY);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClickedMixin(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        DrawTabHelper.INSTANCE.onTabButtonClick(client, this, this.x, this.y, mouseX, mouseY, this.focusedSlot != null);
    }
}