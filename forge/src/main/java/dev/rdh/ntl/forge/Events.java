package dev.rdh.ntl.forge;


import dev.rdh.ntl.CreateNTL;

import dev.rdh.ntl.command.NTLCommands;
import dev.rdh.ntl.config.NTLConfigs;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import net.minecraft.commands.Commands.CommandSelection;

public abstract class Events {
	@Mod.EventBusSubscriber(modid = CreateNTL.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static abstract class ClientModBusEvents {
		@SubscribeEvent
		static void onLoadComplete(FMLLoadCompleteEvent event) {
			ModContainer container = ModList.get()
				.getModContainerById(CreateNTL.ID)
				.orElseThrow(() -> new IllegalStateException("Create NTL mod container missing on LoadComplete"));
			container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory(NTLConfigs::createConfigScreen));
		}
	}

	@SubscribeEvent
	static void registerCommands(RegisterCommandsEvent event) {
		CommandSelection selection = event.getCommandSelection();
		boolean dedicated = selection == CommandSelection.ALL || selection == CommandSelection.DEDICATED;
		NTLCommands.register(event.getDispatcher(), dedicated);
	}

	@SubscribeEvent
	static void onConfigLoad(ModConfigEvent.Loading event) {
		NTLConfigs.onLoad(event.getConfig());
	}

	@SubscribeEvent
	static void onConfigReload(ModConfigEvent.Reloading event) {
		NTLConfigs.onReload(event.getConfig());
	}
}