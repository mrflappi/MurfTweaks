package net.murfgames.murftweaks.common.handshake;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientHandshake {
    private static boolean serverHasMod = false;

    public static void register() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            // Send hello on join
            ClientPlayConnectionEvents.JOIN.register((handler, sender, thisClient) -> {
                serverHasMod = false;
                ClientPlayNetworking.send(new HandshakeC2SPayload());
            });

            ClientPlayConnectionEvents.DISCONNECT.register((handler, thisClient) -> {
                serverHasMod = false;
            });

            // Listen for server reply
            ClientPlayNetworking.registerGlobalReceiver(HandshakeS2CPayload.ID, (payload, context) -> {
                serverHasMod = true;
            });
        });


    }

    public static boolean isServerModded() {
        return serverHasMod;
    }
}
