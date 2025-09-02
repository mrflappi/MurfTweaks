package net.murfgames.murftweaks.persistentenchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class PersistentHelper {
    public static final int MIN_DURABILITY = 1;
    public static final TagKey<Enchantment> PREVENTS_BREAK = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(PersistentEnchantmentModule.PACKAGE_ID, "prevents_break"));

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
