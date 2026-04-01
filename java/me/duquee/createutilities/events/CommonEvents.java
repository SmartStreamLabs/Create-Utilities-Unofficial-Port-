package me.duquee.createutilities.events;

import me.duquee.createutilities.CreateUtilities;
import me.duquee.createutilities.blocks.voidtypes.battery.VoidBatteryData;
import me.duquee.createutilities.blocks.voidtypes.chest.VoidChestInventoriesData;
import me.duquee.createutilities.blocks.voidtypes.tank.VoidTanksData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.neoforge.event.level.LevelEvent;
public class CommonEvents {

	public static void onLoad(LevelEvent.Load event) {

		MinecraftServer server = event.getLevel().getServer();
		if (server == null) return;

		LevelAccessor level = event.getLevel();
		DimensionDataStorage dataStorage = server.overworld().getDataStorage();

		CreateUtilities.VOID_MOTOR_LINK_NETWORK_HANDLER.onLoadWorld(level);

		CreateUtilities.VOID_CHEST_INVENTORIES_DATA = dataStorage
				.computeIfAbsent(new SavedData.Factory<>(VoidChestInventoriesData::new, VoidChestInventoriesData::load), "VoidChestInventories");

		CreateUtilities.VOID_TANKS_DATA = dataStorage
				.computeIfAbsent(new SavedData.Factory<>(VoidTanksData::new, VoidTanksData::load), "VoidTanks");

		CreateUtilities.VOID_BATTERIES_DATA = dataStorage
				.computeIfAbsent(new SavedData.Factory<>(VoidBatteryData::new, VoidBatteryData::load), "VoidBatteries");

	}

	public static void onUnload(LevelEvent.Unload event) {
		CreateUtilities.VOID_MOTOR_LINK_NETWORK_HANDLER.onUnloadWorld(event.getLevel());
	}

}
