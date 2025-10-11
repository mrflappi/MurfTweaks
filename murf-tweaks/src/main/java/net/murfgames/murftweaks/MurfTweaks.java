package net.murfgames.murftweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.util.Identifier;
import net.murfgames.bibliomurf.BiblioModule;
import net.murfgames.bibliomurf.BiblioMurf;
import net.murfgames.murftweaks.persistentenchantment.PersistentEnchantmentModule;
import net.murfgames.murftweaks.snowsettings.SnowSettingsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MurfTweaks implements ModInitializer, PreLaunchEntrypoint, BiblioModule {
    public static final String MOD_ID = "murf-tweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier MODULE_ID = Identifier.of(MOD_ID, "package");

    @Override
    public void onInitialize() {
        LOGGER.info("MurfTweaks is initialized!");
    }

    @Override
    public void onPreLaunch() {
        registerModule();
    }

    @Override
    public Identifier getID() {
        return MODULE_ID;
    }

    @Override
    public String getVersion() {
        return "0.1.2";
    }
}
