package net.murfgames.murftweaks;

import net.fabricmc.api.ClientModInitializer;
import net.murfgames.murftweaks.common.MurfTweaksCommonClient;
import net.murfgames.murftweaks.common.handshake.ClientHandshake;

public class MurfTweaksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientHandshake.register();
    }
}
