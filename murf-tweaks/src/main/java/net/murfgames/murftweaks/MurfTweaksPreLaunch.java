package net.murfgames.murftweaks;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.murfgames.murftweaks.common.MurfTweaksCommon;

public class MurfTweaksPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        MurfTweaksCommon.registerModule(new MurfTweaks());
    }
}
