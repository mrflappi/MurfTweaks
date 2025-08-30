package net.murfgames.murftweaks.enchantment;

import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PersistentHelper {
    public static final int MIN_DURABILITY = 1;

    public static void syncPlayerInventory(ServerPlayerEntity serverPlayer) {
        serverPlayer.currentScreenHandler.sendContentUpdates();
        serverPlayer.playerScreenHandler.sendContentUpdates();

        serverPlayer.networkHandler.sendPacket(
                new InventoryS2CPacket(
                        serverPlayer.currentScreenHandler.syncId,
                        serverPlayer.currentScreenHandler.getRevision(),
                        serverPlayer.currentScreenHandler.getStacks(),
                        serverPlayer.currentScreenHandler.getCursorStack()
                        
                )
        );
    }
}
