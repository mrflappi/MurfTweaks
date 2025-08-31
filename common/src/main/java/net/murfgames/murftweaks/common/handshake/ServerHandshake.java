package net.murfgames.murftweaks.common.handshake;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.murfgames.murftweaks.common.MurfTweaksCommon;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandshake {
    private static final Set<ServerPlayerEntity> MODDED_PLAYERS = ConcurrentHashMap.newKeySet();

    public static void register() {
        PayloadTypeRegistry.playC2S().register(HandshakeC2SPayload.ID, HandshakeC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HandshakeS2CPayload.ID, HandshakeS2CPayload.CODEC);

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
    }


    public static boolean hasMod(ServerPlayerEntity player) {
        return MODDED_PLAYERS.contains(player);
    }
}
