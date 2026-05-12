package net.murfgames.murftweaks.snowsettings;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gamerules.GameRule;
import java.util.Optional;

public abstract class SnowHelper {
    public static final TagKey<Block> ALLOWS_SNOWFALL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(SnowSettingsModule.PACKAGE_ID, "allows_snowfall"));
    public static final TagKey<Block> PREVENTS_SNOWFALL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(SnowSettingsModule.PACKAGE_ID, "prevents_snowfall"));
    private static boolean whitelistSnowfall = false;

    public static GameRule<Boolean> DO_SNOW_MELTING;

    public static void register() {
        DO_SNOW_MELTING = GameRuleBuilder.forBoolean(false)
                .buildAndRegister(Identifier.fromNamespaceAndPath(SnowSettingsModule.PACKAGE_ID, "do_snow_melting"));

        ServerLifecycleEvents.SERVER_STARTED.register(SnowHelper::cacheWhitelistSnowfall);

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
            if (success)
                cacheWhitelistSnowfall(server);
        });
    }

    private static void cacheWhitelistSnowfall(MinecraftServer server) {
        Optional<Registry<Block>> blockRegistry = server.registryAccess().lookup(Registries.BLOCK);

        whitelistSnowfall = false;
        blockRegistry.ifPresent(registry -> {
            whitelistSnowfall = registry.entrySet().stream().anyMatch(entry -> entry.getValue().defaultBlockState().is(ALLOWS_SNOWFALL));
        });
    }

    public static boolean doWhitelistSnowfall() {
        return whitelistSnowfall;
    }
}
