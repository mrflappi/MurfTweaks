package net.murfgames.murftweaks.snowsettings;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class SnowHelper {
    public static final TagKey<Block> ALLOWS_SNOWFALL = TagKey.of(RegistryKeys.BLOCK, Identifier.of(SnowSettingsModule.PACKAGE_ID, "allows_snowfall"));
    public static final TagKey<Block> PREVENTS_SNOWFALL = TagKey.of(RegistryKeys.BLOCK, Identifier.of(SnowSettingsModule.PACKAGE_ID, "prevents_snowfall"));
    private static boolean whitelistSnowfall = false;

    public static void registerReload() {
        ServerLifecycleEvents.SERVER_STARTED.register(SnowHelper::cacheWhitelistSnowfall);

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
            if (success)
                cacheWhitelistSnowfall(server);
        });
    }

    private static void cacheWhitelistSnowfall(MinecraftServer server) {
        Optional<Registry<Block>> blockRegistry = server.getRegistryManager().getOptional(RegistryKeys.BLOCK);

        whitelistSnowfall = false;
        blockRegistry.ifPresent(registry -> {
            whitelistSnowfall = registry.getEntrySet().stream().anyMatch(entry -> entry.getValue().getDefaultState().isIn(ALLOWS_SNOWFALL));
        });
    }

    public static boolean doWhitelistSnowfall() {
        return whitelistSnowfall;
    }
}
