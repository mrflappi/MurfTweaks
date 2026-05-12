package net.murfgames.murftweaks.persistentenchantment.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.murfgames.murftweaks.persistentenchantment.mixinhelper.ItemStackExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.world.entity.animal.armadillo.Armadillo.class)
public abstract class ArmadilloEntityMixin {
    @ModifyExpressionValue(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean modifyExpressionValue_interactMob(boolean original, Player player, InteractionHand hand) {
        // original = result of itemStack.isOf(Items.BRUSH)
        ItemStack itemStack = player.getItemInHand(hand);
        boolean extraCondition = !((ItemStackExtender)(Object)itemStack).murf_tweaks$isPersistentBroken();
        return original && extraCondition;
    }
}
