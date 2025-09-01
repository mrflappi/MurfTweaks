package net.murfgames.murftweaks.common.handshake;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.murfgames.murftweaks.common.MurfTweaksCommon;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandshake {
    private static final Set<ServerPlayerEntity> MODDED_PLAYERS = ConcurrentHashMap.newKeySet();
    private static boolean registered = false;

    public static void register() {
        if (registered)
            return;

        HandshakeRegister.registerPayloadTypes();

        ServerPlayNetworking.registerGlobalReceiver(HandshakeC2SPayload.ID, (payload, context) -> {
            context.player().getServer().execute(() -> {
                MODDED_PLAYERS.add(context.player());
                MurfTweaksCommon.LOGGER.info("{} has MurfTweaks installed!", context.player().getName());
                ServerPlayNetworking.send(context.player(), new HandshakeS2CPayload());
            });
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            MODDED_PLAYERS.remove(handler.player);
        });

        registered = true;
    }


    public static boolean hasMod(ServerPlayerEntity player) {
        return MODDED_PLAYERS.contains(player);
    }
}
