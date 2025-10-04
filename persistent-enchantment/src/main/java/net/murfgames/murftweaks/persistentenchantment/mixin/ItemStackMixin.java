package net.murfgames.murftweaks.persistentenchantment.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.murfgames.murftweaks.persistentenchantment.PersistentHelper;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExtender {

    // Check if item with persistent enchantment should be considered 'broken'
    @Unique
    @Override
    public boolean murf_tweaks$isPersistentBroken() {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.isOf(Items.ELYTRA)) return false;

        boolean shouldBreak = stack.isDamageable() && stack.getDamage() >= stack.getMaxDamage() - 1;
        return shouldBreak && EnchantmentHelper.hasAnyEnchantmentsIn(stack, PersistentHelper.PREVENTS_BREAK);
    }

    // Sync inventory visual with player after a change
    @Unique
    private void syncInventory(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer)
            PersistentHelper.syncPlayerInventory(serverPlayer);
    }

    // Remove broken item with persistent enchantment from armor slot
    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void inject_inventoryTick(World world, Entity entity, EquipmentSlot slot, CallbackInfo ci) {
        if (world != null && slot != null) {
            ItemStack stack = (ItemStack) (Object) this;

            if (!world.isClient() && murf_tweaks$isPersistentBroken() && entity instanceof LivingEntity livingEntity && slot.isArmorSlot()) {
                ItemStack newStack = stack.copyAndEmpty();
                livingEntity.giveOrDropStack(newStack);

                if (livingEntity instanceof ServerPlayerEntity serverPlayer)
                    serverPlayer.sendEquipmentBreakStatus(newStack.getItem(), slot);
            }
        }
    }

    // Prevent item from actually breaking with persistent enchantment
    @Inject(method = "shouldBreak", at = @At("RETURN"), cancellable = true)
    private void inject_shouldBreak(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            ItemStack stack = (ItemStack)(Object)this;
            if (EnchantmentHelper.hasAnyEnchantmentsIn(stack, PersistentHelper.PREVENTS_BREAK)) {
                cir.setReturnValue(false);
            }
        }
    }

    // Set item damage to minimum with persistent enchantment
    @Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V", at = @At("TAIL"))
    private void inject_damage(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
        if (!entity.getWorld().isClient()) {
            ItemStack stack = (ItemStack) (Object) this;
            if (murf_tweaks$isPersistentBroken() && stack.getDamage() > stack.getMaxDamage() - PersistentHelper.MIN_DURABILITY) {
                stack.setDamage(stack.getMaxDamage() - PersistentHelper.MIN_DURABILITY);
            }
        }
    }

    // Prevent broken item from being used with persistent enchantment
    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void inject_use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient() && murf_tweaks$isPersistentBroken() && Objects.requireNonNull(user.getGameMode()).isSurvivalLike()) {
            cir.setReturnValue(ActionResult.PASS);
            syncInventory(user);
        }
    }

    // Prevent broken item from being used on a block with persistent enchantment
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void inject_useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!context.getWorld().isClient() && murf_tweaks$isPersistentBroken()) {
            if (context.getPlayer() == null || Objects.requireNonNull(context.getPlayer().getGameMode()).isSurvivalLike())
                cir.setReturnValue(ActionResult.PASS);
        }
    }

    // Prevent broken item from being used on an entity with persistent enchantment
    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    private void inject_useOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!entity.getWorld().isClient() && murf_tweaks$isPersistentBroken()  && Objects.requireNonNull(user.getGameMode()).isSurvivalLike())
            cir.setReturnValue(ActionResult.PASS);
    }

    // Prevent broken item from mining with persistent enchantment
    @Inject(method = "canMine", at = @At("HEAD"), cancellable = true)
    private void inject_canMine(BlockState state, World world, BlockPos pos, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!world.isClient() && murf_tweaks$isPersistentBroken()  && Objects.requireNonNull(player.getGameMode()).isSurvivalLike())
            cir.setReturnValue(false);
    }
}
