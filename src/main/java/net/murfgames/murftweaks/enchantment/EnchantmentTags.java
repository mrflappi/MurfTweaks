package net.murfgames.murftweaks.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public interface EnchantmentTags {
    TagKey<Enchantment> PREVENTS_BREAK = of("prevents_break");

    private static TagKey<Enchantment> of(String id) {
        return TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("murf-tweaks", id));
    }
}
