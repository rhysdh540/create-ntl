package dev.rdh.ntl.forge;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.rdh.ntl.CreateNTL;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;

import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.entity.ai.attributes.Attribute;

import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static dev.rdh.ntl.multiversion.SupportedMinecraftVersion.*;

@SuppressWarnings({"UnstableApiUsage", "RedundantSuppression"})
public class UtilImpl {

	public static void registerConfig(ModConfig.Type type, IConfigSpec<?> spec) {
		ModLoadingContext.get().registerConfig(type, spec);
	}

	public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
	void registerArgument(Class<A> clazz, I info, ResourceLocation id) {
		ArgumentTypeInfos.registerByClass(clazz, info);
	}

	public static String getVersion(String modid) {
		String versionString = "UNKNOWN";

		List<IModInfo> infoList = ModList.get().getModFileById(modid).getMods();
		if (infoList.size() > 1) {
			CreateNTL.LOGGER.error("Multiple mods for ID: " + modid);
		}
		for (IModInfo info : infoList) {
			if (info.getModId().equals(modid)) {
				versionString = info.getVersion().toString();
				break;
			}
		}
		return versionString;
	}

	public static boolean isDevEnv() {
		return !FMLLoader.isProduction();
	}

	public static String platformName() {
		return "Forge";
	}

	public static boolean isModLoaded(String modid) {
		return ModList.get().isLoaded(modid) || LoadingModList.get().getModFileById(modid) != null;
	}
}
