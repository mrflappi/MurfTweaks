package net.murfgames.murftweaks.compactshulkers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.murfgames.bibliomurf.BiblioModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompactShulkersModule implements ModInitializer, PreLaunchEntrypoint, BiblioModule {

    public static final String PACKAGE_ID = "murf-tweaks";
    public static final String MOD_ID = "compact-shulkers";
    public static final String MOD_VERSION = "0.3.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier MODULE_ID = Identifier.fromNamespaceAndPath(PACKAGE_ID, MOD_ID);

    @Override
    public void onInitialize() {
        CompactShulkerBoxCreator.onInitialise();
    }

    @Override
    public void onPreLaunch() {
        registerModule();
    }

    @Override
    public Identifier getID() {
        return MODULE_ID;
    }

    @Override
    public String getVersion() {
        return MOD_VERSION;
    }
}
