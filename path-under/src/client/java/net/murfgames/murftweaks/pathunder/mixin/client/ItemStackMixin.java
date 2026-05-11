package net.murfgames.murftweaks.pathunder.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.murfgames.murftweaks.pathunder.PathUnderModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ShovelItem.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract ActionResult useOnBlock(ItemUsageContext context);

    @Shadow
    @Final
    protected static Map<Block, BlockState> PATH_STATES;

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void inject_useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getWorld().isClient() && context.getPlayer() != null) {
            World world = context.getWorld();

            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

            BlockPos lowerBlockPos = context.getBlockPos().down();
            BlockState lowerBlockState = (BlockState) PATH_STATES.get(world.getBlockState(lowerBlockPos).getBlock());

            if (lowerBlockState != null && blockState.isIn(BlockTags.REPLACEABLE)) {
                world.breakBlock(blockPos, true);

                BlockHitResult newHit = new BlockHitResult(Vec3d.ofCenter(lowerBlockPos), Direction.UP, lowerBlockPos, context.hitsInsideBlock());
                ItemUsageContext newContext = new ItemUsageContext(context.getPlayer(), context.getHand(), newHit);
                useOnBlock(newContext);

                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
