package net.murfgames.murftweaks.snowsettings;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public abstract class SnowHelper {
    public static final TagKey<Block> ALLOWS_SNOWFALL = TagKey.of(RegistryKeys.BLOCK, Identifier.of(SnowSettingsModule.PACKAGE_ID, "allows_snowfall"));
}
