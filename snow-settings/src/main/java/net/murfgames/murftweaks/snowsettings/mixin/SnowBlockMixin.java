package net.murfgames.murftweaks.snowsettings.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.murfgames.murftweaks.snowsettings.SnowHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void inject_randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!world.getGameRules().getBoolean(SnowHelper.DO_SNOW_MELTING))
            ci.cancel();
    }

}
