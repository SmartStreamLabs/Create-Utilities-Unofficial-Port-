package me.duquee.createutilities;

import me.duquee.createutilities.blocks.CUPartialsModels;
import me.duquee.createutilities.blocks.voidtypes.VoidStorageClient;
import me.duquee.createutilities.blocks.voidtypes.battery.VoidBattery;
import me.duquee.createutilities.blocks.voidtypes.tank.VoidTank;
import me.duquee.createutilities.ponder.CUPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateUtilitiesClient {

	public static final VoidStorageClient<FluidTank> VOID_TANKS = new VoidStorageClient<>(
			k -> new FluidTank(VoidTank.CAPACITY));

	public static final VoidStorageClient<VoidBattery> VOID_BATTERIES = new VoidStorageClient<>(
			VoidBattery::new);

	public static void onCtorClient(IEventBus modEventBus) {
		modEventBus.addListener(CreateUtilitiesClient::clientInit);
		CUPartialsModels.init();
	}

	public static void clientInit(final FMLClientSetupEvent event) {
		PonderIndex.addPlugin(new CUPonderPlugin());
	}

}
