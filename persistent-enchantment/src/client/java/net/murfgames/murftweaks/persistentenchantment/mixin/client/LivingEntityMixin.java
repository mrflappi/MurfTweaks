package net.murfgames.murftweaks.persistentenchantment.mixin.client;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.murfgames.bibliomurf.handshake.ClientHandshake;
import net.murfgames.murftweaks.persistentenchantment.PersistentEnchantmentModule;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "isEquippableInSlot", at = @At("HEAD"), cancellable = true)
    private void inject_canEquip(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        boolean isOnServer = false;
        if (entity.level().isClientSide())
            isOnServer = ClientHandshake.serverHasModule(PersistentEnchantmentModule.MODULE_ID);

        if ((entity.level().isClientSide() || isOnServer) && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(false);
        }
    }
}
