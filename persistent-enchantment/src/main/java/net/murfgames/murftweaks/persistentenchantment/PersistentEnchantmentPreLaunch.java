package net.murfgames.murftweaks.persistentenchantment;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.murfgames.murftweaks.common.MurfTweaksCommon;
import net.murfgames.murftweaks.common.MurfTweaksCommonClient;

public class PersistentEnchantmentPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        MurfTweaksCommon.registerModule(new PersistentEnchantmentModule());
    }
}
