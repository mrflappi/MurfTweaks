package net.murfgames.murftweaks.common.handshake;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class HandshakeRegister {
    private static boolean registered = false;

    public static void registerPayloadTypes() {
        if (registered) return;

        // Both sides must know both payloads
        PayloadTypeRegistry.playC2S().register(HandshakeC2SPayload.ID, HandshakeC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HandshakeS2CPayload.ID, HandshakeS2CPayload.CODEC);

        registered = true;
    }
}
