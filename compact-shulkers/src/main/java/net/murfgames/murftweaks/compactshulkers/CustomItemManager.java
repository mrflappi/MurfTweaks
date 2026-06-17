package net.murfgames.murftweaks.compactshulkers;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.Optional;

public abstract class CustomItemManager {

    private static final Identifier CUSTOM_ITEM_KEY = Identifier.fromNamespaceAndPath(CompactShulkersModule.PACKAGE_ID, "custom_item");

    public static CompoundTag createCustomItemTag(Identifier identifier) {
        CompoundTag tag = new CompoundTag();
        tag.putString(
                CUSTOM_ITEM_KEY.toString(),
                identifier.toString()
        );
        return tag;
    }

    public static Optional<Identifier> getCustomItem(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);

        if (customData != null) {
            Optional<String> customItem = customData.copyTag().getString(CUSTOM_ITEM_KEY.toString());
            if (customItem.isPresent())
                return Optional.ofNullable(Identifier.tryParse(customItem.get()));
        }

        return Optional.empty();
    }
}
