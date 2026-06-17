package net.murfgames.murftweaks.compactshulkers;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.Optional;

public class CompactShulkerBoxCreator {

    private static final Identifier COMPACT_SHULKER_IDENTIFIER = Identifier.fromNamespaceAndPath(CompactShulkersModule.PACKAGE_ID, "compact_shulker_box");
    private static final ItemStackTemplate COMPACT_SHULKER_TEMPLATE = new ItemStackTemplate(
            Items.MUSIC_DISC_5,
            DataComponentPatch.builder()
                    .remove(DataComponents.JUKEBOX_PLAYABLE)
                    .set(DataComponents.ITEM_MODEL, Identifier.withDefaultNamespace("shulker_box"))
                    .set(DataComponents.ITEM_NAME, Component.translatableWithFallback("item.murf-tweaks.compact_shulker_box", "Compact Shulker Box"))
                    .set(DataComponents.RARITY, Rarity.COMMON)
                    .set(DataComponents.CUSTOM_DATA, CustomData.of(CustomItemManager.createCustomItemTag(COMPACT_SHULKER_IDENTIFIER)))
                    .build());

    private static final Command<CommandSourceStack> COMPACTOR_COMMAND = commandContext -> {
        try {
            if (commandContext.getSource().isPlayer()) {
                ServerPlayer player = commandContext.getSource().getPlayerOrException();
                ItemStack stack = player.getMainHandItem();
                Optional<ItemStack> decompacted = tryDecompactShulkerBox(stack);
                decompacted.ifPresentOrElse(
                        itemStack -> player.setItemInHand(InteractionHand.MAIN_HAND, itemStack),
                        () -> {
                            Optional<ItemStack> compacted = tryCompactShulkerBox(stack);
                            compacted.ifPresent(
                                    itemStack ->  player.setItemInHand(InteractionHand.MAIN_HAND, itemStack)
                            );
                        });
            }

            return Command.SINGLE_SUCCESS;
        } catch (CommandSyntaxException e) {
            return 0;
        }
    };

    public static void onInitialise() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("murf-tweaks")
                    .then(Commands.literal("compact")
                            .executes(COMPACTOR_COMMAND)));
        });
    }

    public static Optional<ItemStack> tryDecompactShulkerBox(ItemStack itemStack) {
        Optional<Identifier> customItemId = CustomItemManager.getCustomItem(itemStack);

        if (customItemId.isEmpty() || !customItemId.get().equals(COMPACT_SHULKER_IDENTIFIER))
            return Optional.empty();

        ItemStack newStack = new ItemStack(Items.SHULKER_BOX);
        newStack.copyFrom(DataComponents.CONTAINER, itemStack);
        return Optional.of(newStack);
    }

    public static Optional<ItemStack> tryCompactShulkerBox(ItemStack itemStack) {
        if (itemStack.is(ItemTags.SHULKER_BOXES)) {
            ItemStack newStack = COMPACT_SHULKER_TEMPLATE.apply(DataComponentPatch.builder()
                    .set(DataComponents.CONTAINER, itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY))
                    .build());
            return Optional.of(newStack);
        }

        return Optional.empty();
    }
}
