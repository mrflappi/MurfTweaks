package net.murfgames.murftweaks.persistentenchantment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.murfgames.murftweaks.common.MurfTweaksCommon;
import net.murfgames.murftweaks.common.MurfTweaksModule;
import net.murfgames.murftweaks.common.handshake.ServerHandshake;

public class PersistentEnchantmentModule implements ModInitializer, PreLaunchEntrypoint, MurfTweaksModule {

    @Override
    public void onPreLaunch() {
        MurfTweaksCommon.registerModule(new PersistentEnchantmentModule());
    }

    @Override
    public void onInitialize() {

    }

    @Override
    public void onMurfInitialize() {
        MurfTweaksCommon.LOGGER.info("Persistent Enchantment is present");
    }
}
