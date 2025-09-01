package net.murfgames.murftweaks.persistentenchantment.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.entity.passive.ArmadilloEntity.class)
public abstract class ArmadilloEntityMixin {
    @ModifyExpressionValue(
            method = "interactMob",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
    )
    private boolean modifyExpressionValue_interactMob(boolean original, PlayerEntity player, Hand hand) {
        // original = result of itemStack.isOf(Items.BRUSH)
        ItemStack itemStack = player.getStackInHand(hand);
        boolean extraCondition = !((ItemStackExtender)(Object)itemStack).murf_tweaks$isPersistentBroken();
        return original && extraCondition;
    }
}
