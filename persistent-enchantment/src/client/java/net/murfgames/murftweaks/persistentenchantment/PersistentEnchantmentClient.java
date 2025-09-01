package net.murfgames.murftweaks.persistentenchantment;

import net.fabricmc.api.ClientModInitializer;
import net.murfgames.murftweaks.common.handshake.ClientHandshake;

public class PersistentEnchantmentClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientHandshake.register();
    }
}
