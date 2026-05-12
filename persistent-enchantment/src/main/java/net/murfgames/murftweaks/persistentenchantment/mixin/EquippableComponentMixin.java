package net.murfgames.murftweaks.persistentenchantment.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.murfgames.bibliomurf.handshake.ServerHandshake;
import net.murfgames.murftweaks.persistentenchantment.PersistentEnchantmentModule;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equippable.class)
public abstract class EquippableComponentMixin {

    @Inject(method = "swapWithEquipmentSlot", at = @At("HEAD"), cancellable = true)
    private void inject_equip(ItemStack stack, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        boolean isOnClient = false;
        if (player instanceof ServerPlayer serverPlayer)
            isOnClient = ServerHandshake.playerHasModule(PersistentEnchantmentModule.MODULE_ID, serverPlayer);

        if (isOnClient && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
