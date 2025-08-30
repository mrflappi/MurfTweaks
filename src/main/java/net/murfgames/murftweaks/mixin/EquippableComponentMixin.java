package net.murfgames.murftweaks.mixin;

import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.murfgames.murftweaks.MurfTweaks;
import net.murfgames.murftweaks.handshake.ServerHandshake;
import net.murfgames.murftweaks.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EquippableComponent.class)
public abstract class EquippableComponentMixin {

    @Inject(method = "equip", at = @At("HEAD"), cancellable = true)
    private void inject_equip(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
        boolean isOnClient = false;
        if (player instanceof ServerPlayerEntity serverPlayer)
            isOnClient = ServerHandshake.hasMod(serverPlayer);

        if (isOnClient && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
