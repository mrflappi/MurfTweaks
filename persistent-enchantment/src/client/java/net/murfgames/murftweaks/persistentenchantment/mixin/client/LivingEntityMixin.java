package net.murfgames.murftweaks.persistentenchantment.mixin.client;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.murfgames.bibliomurf.handshake.ClientHandshake;
import net.murfgames.murftweaks.persistentenchantment.PersistentEnchantmentModule;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "canEquip", at = @At("HEAD"), cancellable = true)
    private void inject_canEquip(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        boolean isOnServer = false;
        if (entity.getWorld().isClient())
            isOnServer = ClientHandshake.serverHasModule(PersistentEnchantmentModule.MODULE_ID);

        if ((entity.getWorld().isClient() || isOnServer) && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(false);
        }
    }
}
