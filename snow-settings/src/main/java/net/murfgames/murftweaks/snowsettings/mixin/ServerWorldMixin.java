package net.murfgames.murftweaks.snowsettings.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.murfgames.murftweaks.snowsettings.SnowHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
    @Redirect(
            method = "tickPrecipitation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
                    ordinal = 2
            )
    )
    private boolean redirectSnowPlacement_tickIceAndSnow(ServerLevel instance, BlockPos blockPos, BlockState blockState) {
        BlockState belowState = instance.getBlockState(blockPos.below());

        if ((SnowHelper.doWhitelistSnowfall() && belowState.is(SnowHelper.ALLOWS_SNOWFALL)) || (!SnowHelper.doWhitelistSnowfall() && !belowState.is(SnowHelper.PREVENTS_SNOWFALL))) {
            return instance.setBlockAndUpdate(blockPos, blockState);
        }

        return false;
    }
}
