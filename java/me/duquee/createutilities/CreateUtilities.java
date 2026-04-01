package me.duquee.createutilities;

import com.simibubi.create.foundation.data.CreateRegistrate;
import me.duquee.createutilities.blocks.CUBlocks;
import me.duquee.createutilities.blocks.CUTileEntities;
import me.duquee.createutilities.blocks.voidtypes.CUContainerTypes;
import me.duquee.createutilities.blocks.voidtypes.battery.VoidBattery;
import me.duquee.createutilities.blocks.voidtypes.battery.VoidBatteryData;
import me.duquee.createutilities.blocks.voidtypes.chest.VoidChestInventory;
import me.duquee.createutilities.blocks.voidtypes.chest.VoidChestInventoriesData;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler;
import me.duquee.createutilities.blocks.voidtypes.tank.VoidTank;
import me.duquee.createutilities.blocks.voidtypes.tank.VoidTanksData;
import me.duquee.createutilities.events.ClientEvents;
import me.duquee.createutilities.events.CommonEvents;
import me.duquee.createutilities.items.CUItems;
import me.duquee.createutilities.mountedstorage.CUMountedStorages;
import me.duquee.createutilities.networking.CUPackets;
import me.duquee.createutilities.tabs.CUCreativeTabs;
import me.duquee.createutilities.voidlink.VoidLinkHandler;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

@Mod(CreateUtilities.ID)
public class CreateUtilities {

	public static final String ID = "createutilities";
	public static final String NAME = "Create Utilities (Unofficial Port)";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);

	public static final VoidMotorNetworkHandler VOID_MOTOR_LINK_NETWORK_HANDLER = new VoidMotorNetworkHandler();
	public static VoidChestInventoriesData VOID_CHEST_INVENTORIES_DATA;

	public static VoidTanksData VOID_TANKS_DATA;
	public static VoidBatteryData VOID_BATTERIES_DATA;

	public CreateUtilities(IEventBus modEventBus) {
		modEventBus.addListener(CreateUtilities::onBuildCreativeModeTabContents);
		REGISTRATE.registerEventListeners(modEventBus);

		CUBlocks.register();
		CUItems.register();
		CUTileEntities.register();
		CUContainerTypes.register();
		CUCreativeTabs.register(modEventBus);
		CUMountedStorages.register();

		modEventBus.addListener(CreateUtilities::registerCapabilities);
		modEventBus.addListener(CUPackets::register);

		NeoForge.EVENT_BUS.addListener(CommonEvents::onLoad);
		NeoForge.EVENT_BUS.addListener(CommonEvents::onUnload);
		NeoForge.EVENT_BUS.addListener(VoidLinkHandler::onBlockActivated);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			CreateUtilitiesClient.onCtorClient(modEventBus);
			NeoForge.EVENT_BUS.addListener(ClientEvents::onTick);
		}
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CUTileEntities.VOID_CHEST.get(),
				(blockEntity, side) -> blockEntity.getItemStorage());
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, CUTileEntities.VOID_TANK.get(),
				(blockEntity, side) -> blockEntity.getFluidStorage());
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CUTileEntities.VOID_BATTERY.get(),
				(blockEntity, side) -> blockEntity.getBattery());
	}

	private static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
		disableRegistrateCreativeTabPopulation();
	}

	private static void disableRegistrateCreativeTabPopulation() {
		REGISTRATE.setCreativeTab(null);
		try {
			Field field = com.tterrag.registrate.AbstractRegistrate.class.getDeclaredField("creativeModeTabModifiers");
			field.setAccessible(true);
			Object value = field.get(REGISTRATE);
			if (value instanceof com.google.common.collect.Multimap<?, ?> creativeModeTabModifiers) {
				creativeModeTabModifiers.clear();
			}
		} catch (ReflectiveOperationException e) {
			LOGGER.warn("Failed to disable Registrate creative tab population", e);
		}
	}

	public static ResourceLocation asResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
}
