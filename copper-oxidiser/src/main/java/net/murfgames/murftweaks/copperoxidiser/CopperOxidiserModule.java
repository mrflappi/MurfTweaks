package net.murfgames.murftweaks.copperoxidiser;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.resources.Identifier;
import net.murfgames.bibliomurf.BiblioModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopperOxidiserModule implements ModInitializer, PreLaunchEntrypoint, BiblioModule {

    public static final String PACKAGE_ID = "murf-tweaks";
    public static final String MOD_ID = "copper-oxidiser";
    public static final String MOD_VERSION = "0.3.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier MODULE_ID = Identifier.fromNamespaceAndPath(PACKAGE_ID, MOD_ID);

    @Override
    public void onInitialize() {

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
        return MOD_VERSION;
    }
}
