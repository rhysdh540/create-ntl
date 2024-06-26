package dev.rdh.ntl.fabric;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.rdh.ntl.CreateNTL;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static dev.rdh.ntl.multiversion.SupportedMinecraftVersion.*;

public class UtilImpl {

	private static MethodHandle modLoadingContextRegisterConfig;
	private static MethodHandle forgeConfigRegistryRegister;
	private static Object forgeConfigRegistryInstance;

	private static void setupConfigRegistry() {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		try {
			if(v1_19_2 >= CURRENT) {
				Class<?> modLoadingContextClass = Class.forName("net.minecraftforge.api.ModLoadingContext");
				modLoadingContextRegisterConfig = lookup.findStatic(modLoadingContextClass, "registerConfig",
					MethodType.methodType(ModConfig.class, String.class, Type.class, IConfigSpec.class));
			}

			if(v1_20_1 <= CURRENT) {
				Class<?> forgeConfigRegistryClass = Class.forName("fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry");
				forgeConfigRegistryRegister = lookup.findVirtual(forgeConfigRegistryClass, "register",
					MethodType.methodType(ModConfig.class, String.class, Type.class, IConfigSpec.class));
				forgeConfigRegistryInstance = forgeConfigRegistryClass.getField("INSTANCE").get(null);
			}
		} catch (Throwable e) {
			throw unchecked(e);
		}
	}

	@SuppressWarnings("DataFlowIssue")
	public static void registerConfig(ModConfig.Type type, IConfigSpec<?> spec) {
		if(modLoadingContextRegisterConfig == null && forgeConfigRegistryRegister == null) {
			setupConfigRegistry();
		}
		try {
			if(v1_19_2 >= CURRENT) {
				ModConfig ignore = (ModConfig) modLoadingContextRegisterConfig.invokeExact(CreateNTL.ID, type, spec);
			}

			if(v1_20_1 <= CURRENT) {
				//cannot use invokeExact because the instance class only exists in 1.20.1
				forgeConfigRegistryRegister.invoke(forgeConfigRegistryInstance, CreateNTL.ID, type, spec);
			}
		} catch (Throwable e) {
			throw unchecked(e);
		}
	}

	public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
	void registerArgument(Class<A> clazz, I info, ResourceLocation id) {
		ArgumentTypeRegistry.registerArgumentType(id, clazz, info);
	}

	public static String getVersion(String modid) {
		return FabricLoader.getInstance()
			.getModContainer(modid)
			.orElseThrow(() -> new IllegalArgumentException("Mod container for \"" + modid + "\" not found"))
			.getMetadata()
			.getVersion()
			.getFriendlyString();
	}

	public static boolean isDevEnv() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	public static String platformName() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}

	public static boolean isModLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}
}
