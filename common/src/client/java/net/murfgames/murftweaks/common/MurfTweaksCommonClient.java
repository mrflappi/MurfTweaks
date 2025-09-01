package net.murfgames.murftweaks.common;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.murfgames.murftweaks.common.handshake.ClientHandshake;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MurfTweaksCommonClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientHandshake.register();
    }
}
