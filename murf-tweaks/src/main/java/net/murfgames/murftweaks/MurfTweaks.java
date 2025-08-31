package net.murfgames.murftweaks;

import net.fabricmc.api.ModInitializer;
import net.murfgames.murftweaks.common.MurfTweaksCommon;
import net.murfgames.murftweaks.common.MurfTweaksModule;
import net.murfgames.murftweaks.common.handshake.ServerHandshake;

public class MurfTweaks implements MurfTweaksModule {

    @Override
    public void onInitialize() {
        MurfTweaksCommon.LOGGER.info("Murf is ready to Smurf");
    }
}
