package net.murfgames.murftweaks.pathunder.mixin.client;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {

    @Shadow
    public abstract InteractionResult useOn(UseOnContext context);

    @Shadow
    @Final
    protected static Map<Block, BlockState> FLATTENABLES;

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void inject_useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getLevel().isClientSide() && context.getPlayer() != null) {
            Level world = context.getLevel();

            BlockPos blockPos = context.getClickedPos();
            BlockState blockState = world.getBlockState(blockPos);

            BlockPos lowerBlockPos = context.getClickedPos().below();
            BlockState lowerBlockState = (BlockState) FLATTENABLES.get(world.getBlockState(lowerBlockPos).getBlock());

            if (lowerBlockState != null && blockState.is(BlockTags.REPLACEABLE)) {
                world.destroyBlock(blockPos, true);

                BlockHitResult newHit = new BlockHitResult(Vec3.atCenterOf(lowerBlockPos), Direction.UP, lowerBlockPos, context.isInside());
                UseOnContext newContext = new UseOnContext(context.getPlayer(), context.getHand(), newHit);
                useOn(newContext);

                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
