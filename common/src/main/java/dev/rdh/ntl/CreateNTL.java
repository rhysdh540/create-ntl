package dev.rdh.ntl;

import com.simibubi.create.Create;

import dev.rdh.ntl.config.NTLConfigs;

import dev.rdh.ntl.command.EnumArgument;
import dev.rdh.ntl.multiversion.SupportedMinecraftVersion;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CreateNTL {
	public static final String ID = "ntl";
	public static final String NAME = "Create NTL";
	public static final String VERSION = Util.getVersion(ID).split("-build")[0];
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static void init() {
		if(Util.isModLoaded("createunlimited")) {
			LOGGER.error("Create Unlimited is loaded! Disabling {}...", NAME);
			return;
		}
		LOGGER.info("{} v{} initializing! Create version: {} on platform: {}",
				NAME, VERSION, Create.VERSION, Util.platformName());

		LOGGER.info("Detected Minecraft version: {}", SupportedMinecraftVersion.CURRENT);

		EnumArgument.init();
		NTLConfigs.register();
    }

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(ID, path);
	}
}
