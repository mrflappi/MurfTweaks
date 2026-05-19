package net.murfgames.murftweaks.copperoxidiser.mixin;

import com.google.common.collect.BiMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.murfgames.bibliomurf.handshake.ClientHandshake;
import net.murfgames.murftweaks.copperoxidiser.CopperOxidiserModule;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void inject_useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if (level.isClientSide() && !ClientHandshake.serverHasModule(CopperOxidiserModule.MODULE_ID))
            return;

        BlockPos pos = context.getClickedPos();

        if (!playerHasBlockingItemUseIntent(context)) {
            Optional<BlockState> newBlock = this.evaluateNewBlockState(level, pos, player, level.getBlockState(pos));
            if (newBlock.isPresent()) {
                ItemStack itemInHand = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, itemInHand);
                }

                level.setBlock(pos, newBlock.get(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newBlock.get()));
                if (player != null) {
                    itemInHand.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
                cir.cancel();
            }
        }
    }

    @Unique
    private Optional<BlockState> evaluateNewBlockState(final Level level, final BlockPos pos, @Nullable final Player player, final BlockState oldState) {
        Optional<BlockState> scrapedBlock = WeatheringCopper.getNext(oldState.getBlock()).map(s -> s.withPropertiesOf(oldState));
        if (scrapedBlock.isPresent()) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, LevelEvent.PARTICLES_SCRAPE, pos, 0);
            return scrapedBlock;
        } else
            return Optional.empty();
    }

    @Unique
    private static boolean playerHasBlockingItemUseIntent(final UseOnContext context) {
        Player player = context.getPlayer();
        return context.getHand().equals(InteractionHand.MAIN_HAND) && player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS) && !player.isSecondaryUseActive();
    }
}
