package net.murfgames.murftweaks.common.handshake;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.murfgames.murftweaks.common.MurfTweaksCommon;

public record HandshakeS2CPayload() implements CustomPayload {
    public static final Id<HandshakeS2CPayload> ID = new Id<>(Identifier.of(MurfTweaksCommon.MOD_ID, "handshake_s2c"));

    public static final PacketCodec<RegistryByteBuf, HandshakeS2CPayload> CODEC =
            PacketCodec.of((payload, buf) -> {}, buf -> new HandshakeS2CPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
