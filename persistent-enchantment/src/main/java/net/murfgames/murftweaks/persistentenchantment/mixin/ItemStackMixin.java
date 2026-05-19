package net.murfgames.murftweaks.persistentenchantment.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
        if (stack.is(Items.ELYTRA)) return false;

        boolean shouldBreak = stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage() - 1;
        return shouldBreak && EnchantmentHelper.hasTag(stack, PersistentHelper.PREVENTS_BREAK);
    }

    // Sync inventory visual with player after a change
    @Unique
    private void syncInventory(Player player) {
        if (player instanceof ServerPlayer serverPlayer)
            PersistentHelper.syncPlayerInventory(serverPlayer);
    }

    // Remove broken item with persistent enchantment from armor slot
    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void inject_inventoryTick(Level world, Entity entity, EquipmentSlot slot, CallbackInfo ci) {
        if (world != null && slot != null) {
            ItemStack stack = (ItemStack) (Object) this;

            if (PersistentHelper.clientCheck(world) && murf_tweaks$isPersistentBroken() && entity instanceof LivingEntity livingEntity && slot.isArmor()) {
                ItemStack newStack = stack.copyAndClear();
                livingEntity.handleExtraItemsCreatedOnUse(newStack);

                if (livingEntity instanceof ServerPlayer serverPlayer)
                    serverPlayer.onEquippedItemBroken(newStack.getItem(), slot);
            }
        }
    }

    // Prevent item from actually breaking with persistent enchantment
    @Inject(method = "isBroken", at = @At("RETURN"), cancellable = true)
    private void inject_shouldBreak(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            ItemStack stack = (ItemStack)(Object)this;
            if (EnchantmentHelper.hasTag(stack, PersistentHelper.PREVENTS_BREAK)) {
                cir.setReturnValue(false);
            }
        }
    }

    // Set item damage to minimum with persistent enchantment
    @Inject(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", at = @At("TAIL"))
    private void inject_damage(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
        if (PersistentHelper.clientCheck(entity.level())) {
            ItemStack stack = (ItemStack) (Object) this;
            if (murf_tweaks$isPersistentBroken() && stack.getDamageValue() > stack.getMaxDamage() - PersistentHelper.MIN_DURABILITY) {
                stack.setDamageValue(stack.getMaxDamage() - PersistentHelper.MIN_DURABILITY);
            }
        }
    }

    // Prevent broken item from being used with persistent enchantment
    @Inject(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At("HEAD"), cancellable = true)
    private void inject_use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (PersistentHelper.clientCheck(world) && murf_tweaks$isPersistentBroken() && Objects.requireNonNull(user.gameMode()).isSurvival()) {
            cir.setReturnValue(InteractionResult.PASS);
            syncInventory(user);
        }
    }

    // Prevent broken item from being used on a block with persistent enchantment
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void inject_useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (PersistentHelper.clientCheck(context.getLevel()) && murf_tweaks$isPersistentBroken()) {
            if (context.getPlayer() == null || Objects.requireNonNull(context.getPlayer().gameMode()).isSurvival())
                cir.setReturnValue(InteractionResult.PASS);
        }
    }

    // Prevent broken item from being used on an entity with persistent enchantment
    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    private void inject_useOnEntity(Player user, LivingEntity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (PersistentHelper.clientCheck(entity.level()) && murf_tweaks$isPersistentBroken()  && Objects.requireNonNull(user.gameMode()).isSurvival())
            cir.setReturnValue(InteractionResult.PASS);
    }

    // Prevent broken item from mining with persistent enchantment
    @Inject(method = "canDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void inject_canMine(BlockState state, Level world, BlockPos pos, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (PersistentHelper.clientCheck(world) && murf_tweaks$isPersistentBroken()  && Objects.requireNonNull(player.gameMode()).isSurvival())
            cir.setReturnValue(false);
    }
}
