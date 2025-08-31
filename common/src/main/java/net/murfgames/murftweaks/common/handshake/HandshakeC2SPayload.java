package net.murfgames.murftweaks.common.handshake;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.murfgames.murftweaks.common.MurfTweaksCommon;

public record HandshakeC2SPayload() implements CustomPayload {
    public static final CustomPayload.Id<HandshakeC2SPayload> ID = new Id<>(Identifier.of(MurfTweaksCommon.MOD_ID, "handshake_c2s"));

    public static final PacketCodec<RegistryByteBuf, HandshakeC2SPayload> CODEC =
            PacketCodec.of((payload, buf) -> {}, buf -> new HandshakeC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
