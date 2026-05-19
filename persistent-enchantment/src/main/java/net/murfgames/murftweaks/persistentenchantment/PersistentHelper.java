package net.murfgames.murftweaks.persistentenchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.murfgames.bibliomurf.handshake.ClientHandshake;

public abstract class PersistentHelper {
    public static final int MIN_DURABILITY = 1;
    public static final TagKey<Enchantment> PREVENTS_BREAK = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(PersistentEnchantmentModule.PACKAGE_ID, "prevents_break"));

    public static void syncPlayerInventory(ServerPlayer serverPlayer) {
        serverPlayer.containerMenu.broadcastChanges();
        serverPlayer.inventoryMenu.broadcastChanges();

        serverPlayer.connection.send(
                new ClientboundContainerSetContentPacket(
                        serverPlayer.containerMenu.containerId,
                        serverPlayer.containerMenu.getStateId(),
                        serverPlayer.containerMenu.getItems(),
                        serverPlayer.containerMenu.getCarried()
                        
                )
        );
    }

    public static boolean clientCheck(Level level) {
        return (!level.isClientSide()) || ClientHandshake.serverHasModule(PersistentEnchantmentModule.MODULE_ID);
    }
}
