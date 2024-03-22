package dev.rdh.ntl;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.architectury.injectables.annotations.ExpectPlatform;

import com.simibubi.create.foundation.config.ConfigBase.CValue;
import com.simibubi.create.foundation.config.ConfigBase.ConfigBool;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public abstract class Util {

	@ExpectPlatform
	public static String getVersion(String modid) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isDevEnv() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static String platformName() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerConfig(ModConfig.Type type, IConfigSpec<?> spec) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
	void registerArgument(Class<A> clazz, I info, ResourceLocation id) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isModLoaded(String modid) {
		throw new AssertionError();
	}

	public static <V, T extends ConfigValue<V>> V orElse(CValue<V, T> value, V orElse) {
		try {
			return value.get();
		} catch (IllegalStateException e) {
			if(e.message.contains("config")) {
				return orElse;
			}
			throw e;
		}
	}

	public static boolean orTrue(ConfigBool value) {
		return orElse(value, true);
	}

	public static boolean orFalse(ConfigBool value) {
		return orElse(value, false);
	}
}
