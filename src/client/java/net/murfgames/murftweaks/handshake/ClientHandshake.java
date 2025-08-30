package net.murfgames.murftweaks.handshake;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientHandshake {
    private static boolean serverHasMod = false;

    public static void register() {
        // Send hello on join
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            serverHasMod = false;
            ClientPlayNetworking.send(new HandshakeC2SPayload());
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverHasMod = false;
        });

        // Listen for server reply
        ClientPlayNetworking.registerGlobalReceiver(HandshakeS2CPayload.ID, (payload, context) -> {
            serverHasMod = true;
        });
    }

    public static boolean isServerModded() {
        return serverHasMod;
    }
}
