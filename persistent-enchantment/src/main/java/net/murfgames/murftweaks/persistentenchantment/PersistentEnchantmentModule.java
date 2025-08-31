package net.murfgames.murftweaks.persistentenchantment;

import net.fabricmc.api.ModInitializer;
import net.murfgames.murftweaks.common.MurfTweaksCommon;
import net.murfgames.murftweaks.common.MurfTweaksModule;
import net.murfgames.murftweaks.common.handshake.ServerHandshake;

public class PersistentEnchantmentModule implements MurfTweaksModule {

    @Override
    public void onInitialize() {
        MurfTweaksCommon.LOGGER.info("Persistent Enchantment is present");
    }
}
