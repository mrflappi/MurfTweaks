package net.murfgames.murftweaks.persistentenchantment;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PersistentResourcePackLoader {
    public static void register() {
        FabricLoader.getInstance().getModContainer(PersistentEnchantmentModule.MOD_ID).ifPresentOrElse(
                PersistentResourcePackLoader::loadResourcePacks,
                () -> FabricLoader.getInstance().getModContainer(PersistentEnchantmentModule.PACKAGE_ID).ifPresent(
                        PersistentResourcePackLoader::loadResourcePacks
                )
        );
    }

    private static void loadResourcePacks(ModContainer container) {
        ResourceLoader.registerBuiltinPack(
                Identifier.fromNamespaceAndPath(container.getMetadata().getId(), "persistent_add"),
                container,
                Component.nullToEmpty("Add Persistent Enchantment"),
                PackActivationType.NORMAL
        );

        ResourceLoader.registerBuiltinPack(
                Identifier.fromNamespaceAndPath(container.getMetadata().getId(), "persistent_override"),
                container,
                Component.nullToEmpty("Mending to Persistent Enchantment"),
                PackActivationType.DEFAULT_ENABLED
        );
    }
}
