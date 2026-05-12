package net.murfgames.murftweaks.persistentenchantment.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.murfgames.bibliomurf.handshake.ServerHandshake;
import net.murfgames.murftweaks.persistentenchantment.PersistentEnchantmentModule;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "isEquippableInSlot", at = @At("HEAD"), cancellable = true)
    private void inject_canEquip(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        boolean isOnClient = false;
        if (entity instanceof ServerPlayer player)
            isOnClient = ServerHandshake.playerHasModule(PersistentEnchantmentModule.MODULE_ID, player);

        if ((entity.level().isClientSide() || isOnClient) && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)Z"
            )
    )
    private boolean modifyExpressionValue_damage(boolean original, ServerLevel world, DamageSource source, float amount) {
        if (!world.isClientSide() && source.getWeaponItem() != null && !source.getWeaponItem().isEmpty()) {
            if (source.getEntity() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode().isCreative()) {
                return original;
            }

            return ((ItemStackExtender) (Object) source.getWeaponItem()).murf_tweaks$isPersistentBroken() || original;
        }

        return original;
    }
}
