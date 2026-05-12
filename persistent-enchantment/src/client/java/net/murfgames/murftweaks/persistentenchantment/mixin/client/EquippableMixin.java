package net.murfgames.murftweaks.persistentenchantment.mixin.client;

import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.murfgames.bibliomurf.handshake.ClientHandshake;
import net.murfgames.murftweaks.persistentenchantment.PersistentEnchantmentModule;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equippable.class)
public class EquippableMixin {

    @Inject(method = "swapWithEquipmentSlot", at = @At("HEAD"), cancellable = true)
    private void inject_equip(ItemStack stack, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        boolean isOnServer = false;
        if (player.level().isClientSide())
            isOnServer = ClientHandshake.serverHasModule(PersistentEnchantmentModule.MODULE_ID);

        if (isOnServer && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
