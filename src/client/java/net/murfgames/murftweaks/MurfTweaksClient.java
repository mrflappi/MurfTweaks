package net.murfgames.murftweaks;

import net.fabricmc.api.ClientModInitializer;
import net.murfgames.murftweaks.handshake.ClientHandshake;

public class MurfTweaksClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        ClientHandshake.register();
	}
}