package me.duquee.createutilities.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import me.duquee.createutilities.blocks.CUBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CUPonders {

	public static void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {

		PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

		HELPER.addStoryBoard(CUBlocks.VOID_MOTOR, "void_motor", VoidScenes::voidMotor);
		HELPER.addStoryBoard(CUBlocks.VOID_CHEST, "void_chest", VoidScenes::voidChest);
		HELPER.addStoryBoard(CUBlocks.VOID_TANK, "void_tank", VoidScenes::voidTank);
		HELPER.addStoryBoard(CUBlocks.VOID_BATTERY, "void_battery", VoidScenes::voidBattery);

		HELPER.addStoryBoard(CUBlocks.GEARCUBE, "gearcube", GearboxScenes::gearCube);
		HELPER.addStoryBoard(CUBlocks.LSHAPED_GEARBOX, "lshaped_gearbox", GearboxScenes::lShapedGearbox);

	}

}
