package me.duquee.createutilities.blocks.voidtypes.battery;

import org.jetbrains.annotations.NotNull;
import me.duquee.createutilities.blocks.voidtypes.VoidStorageData;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler.NetworkKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class VoidBatteryData extends VoidStorageData<VoidBattery> {

	public VoidBattery computeStorageIfAbsent(NetworkKey key) {
		return super.computeStorageIfAbsent(key, VoidBattery::new);
	}

	@Override
	public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
		return super.save(tag, registries, VoidBattery::isEmpty, (provider, battery) -> battery.serializeNBT());
	}

	public static VoidBatteryData load(CompoundTag tag, HolderLookup.Provider registries) {
		return load(tag, registries, VoidBatteryData::new, VoidBattery::new,
				(provider, battery, batteryTag) -> battery.deserializeNBT(batteryTag));
	}

}
