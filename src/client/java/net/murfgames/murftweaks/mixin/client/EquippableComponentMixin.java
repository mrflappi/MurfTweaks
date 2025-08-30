package net.murfgames.murftweaks.mixin.client;

import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.murfgames.murftweaks.handshake.ClientHandshake;
import net.murfgames.murftweaks.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EquippableComponent.class)
public class EquippableComponentMixin {

    @Inject(method = "equip", at = @At("HEAD"), cancellable = true)
    private void inject_equip(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
        boolean isOnServer = false;
        if (player.getWorld().isClient())
            isOnServer = ClientHandshake.isServerModded();

        if (isOnServer && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
