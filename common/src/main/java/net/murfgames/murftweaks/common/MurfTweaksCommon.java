package net.murfgames.murftweaks.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.murfgames.murftweaks.common.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public final class MurfTweaksCommon implements ModInitializer {
    public static final String MOD_ID = "murf-tweaks-common";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ArrayList<MurfTweaksModule> loadedModules = new ArrayList<>();
    private static boolean initialized = false;

    public static void registerModule(@NotNull MurfTweaksModule module) {
        if (!initialized)
            loadedModules.add(module);
    }

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            ServerHandshake.register();
        loadedModules.forEach(MurfTweaksModule::onInitialize);
        initialized = true;
    }
}
