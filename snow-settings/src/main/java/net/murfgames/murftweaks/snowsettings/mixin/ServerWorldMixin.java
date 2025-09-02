package net.murfgames.murftweaks.snowsettings.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.murfgames.murftweaks.snowsettings.SnowHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Redirect(
            method = "tickIceAndSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z",
                    ordinal = 2
            )
    )
    private boolean redirectSnowPlacement_tickIceAndSnow(ServerWorld instance, BlockPos blockPos, BlockState blockState) {
        BlockState belowState = instance.getBlockState(blockPos.down());

        if (belowState.isIn(SnowHelper.ALLOWS_SNOWFALL)) {
            return instance.setBlockState(blockPos, blockState);
        }

        return false;
    }
}
