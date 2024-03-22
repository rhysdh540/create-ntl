package dev.rdh.ntl.forge;

import dev.rdh.ntl.CreateNTL;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateNTL.ID)
public final class CreateNTLForge {

    public CreateNTLForge() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get()
			.getModEventBus();
		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

		forgeEventBus.register(Events.ClientModBusEvents.class);
		forgeEventBus.register(Events.class);
		modEventBus.addListener(Events.ClientModBusEvents::onLoadComplete);
		CreateNTL.init();
    }
}
