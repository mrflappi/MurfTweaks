package net.murfgames.murftweaks.common;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.murfgames.murftweaks.common.handshake.ClientHandshake;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;

public class MurfTweaksCommonClient implements ClientModInitializer {

    public static final ArrayList<MurfTweaksModule> loadedModules = new ArrayList<>();
    private static boolean initialized = false;

    public static void registerModule(@NotNull MurfTweaksModule module) {
        if (!initialized)
            loadedModules.add(module);
    }

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            ClientHandshake.register();
        loadedModules.forEach(MurfTweaksModule::onInitialize);
        initialized = true;
    }
}
