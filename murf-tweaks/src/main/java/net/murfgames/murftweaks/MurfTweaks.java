package net.murfgames.murftweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.murfgames.murftweaks.common.MurfTweaksCommon;
import net.murfgames.murftweaks.common.MurfTweaksModule;
import net.murfgames.murftweaks.common.handshake.ServerHandshake;

public class MurfTweaks implements ModInitializer, PreLaunchEntrypoint, MurfTweaksModule {

    @Override
    public void onPreLaunch() {
        MurfTweaksCommon.registerModule(this);
    }

    @Override
    public void onInitialize() {
        ServerHandshake.register();
    }

    @Override
    public void onMurfInitialize() {
        MurfTweaksCommon.LOGGER.info("MurfTweaks package mod is installed!");
    }
}
