package net.murfgames.murftweaks.persistentenchantment.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.murfgames.bibliomurf.handshake.ServerHandshake;
import net.murfgames.murftweaks.persistentenchantment.PersistentEnchantmentModule;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "canEquip", at = @At("HEAD"), cancellable = true)
    private void inject_canEquip(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        boolean isOnClient = false;
        if (entity instanceof ServerPlayerEntity player)
            isOnClient = ServerHandshake.playerHasModule(PersistentEnchantmentModule.MODULE_ID, player);

        if ((entity.getEntityWorld().isClient() || isOnClient) && ((ItemStackExtender)(Object)stack).murf_tweaks$isPersistentBroken()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;)Z"
            )
    )
    private boolean modifyExpressionValue_damage(boolean original, ServerWorld world, DamageSource source, float amount) {
        if (!world.isClient() && source.getWeaponStack() != null && !source.getWeaponStack().isEmpty()) {
            if (source.getAttacker() instanceof ServerPlayerEntity serverPlayer && serverPlayer.getGameMode().isCreative()) {
                return original;
            }

            return ((ItemStackExtender) (Object) source.getWeaponStack()).murf_tweaks$isPersistentBroken() || original;
        }

        return original;
    }
}
