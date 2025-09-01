package net.murfgames.murftweaks.common.handshake;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.murfgames.murftweaks.common.MurfTweaksCommon;

public class ClientHandshake {
    private static boolean serverHasMod = false;
    private static boolean registered = false;

    public static void register() {
        if (registered)
            return;

        HandshakeRegister.registerPayloadTypes();

        // Send hello on join
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            serverHasMod = false;
            client.execute(() -> {
                ClientPlayNetworking.send(new HandshakeC2SPayload());
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            serverHasMod = false;
        });

        // Listen for server reply
        ClientPlayNetworking.registerGlobalReceiver(HandshakeS2CPayload.ID, (payload, context) -> {
            serverHasMod = true;
        });

        registered = true;

    }

    public static boolean isServerModded() {
        return serverHasMod;
    }
}
