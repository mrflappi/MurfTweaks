package net.murfgames.murftweaks.snowsettings;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.block.BlockKeys;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.Identifier;
import net.murfgames.bibliomurf.BiblioModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowSettingsModule implements ModInitializer, PreLaunchEntrypoint, BiblioModule {

    public static final String PACKAGE_ID = "murf-tweaks";
    public static final String MOD_ID = "snow-settings";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier MODULE_ID = Identifier.of(PACKAGE_ID, MOD_ID);

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
}
