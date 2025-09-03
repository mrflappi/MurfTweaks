package net.murfgames.murftweaks.persistentenchantment;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
        ResourceManagerHelper.registerBuiltinResourcePack(
                Identifier.of(container.getMetadata().getId(), "persistent_add"),
                container,
                Text.of("Add Persistent Enchantment"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                Identifier.of(container.getMetadata().getId(), "persistent_override"),
                container,
                Text.of("Mending to Persistent Enchantment"),
                ResourcePackActivationType.DEFAULT_ENABLED
        );
    }
}
