package me.duquee.createutilities.blocks.voidtypes.chest;

import me.duquee.createutilities.blocks.voidtypes.VoidStorageData;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler.NetworkKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import org.jetbrains.annotations.NotNull;

public class VoidChestInventoriesData extends VoidStorageData<VoidChestInventory> {

	public VoidChestInventory computeStorageIfAbsent(NetworkKey key) {
		return super.computeStorageIfAbsent(key, VoidChestInventory::new);
	}

	@Override
	public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
		return super.save(tag, registries, VoidChestInventory::isEmpty,
				(provider, inventory) -> inventory.serializeNBT(provider));
	}

	public static VoidChestInventoriesData load(CompoundTag tag, HolderLookup.Provider registries) {
		return load(tag, registries, VoidChestInventoriesData::new, VoidChestInventory::new,
				(provider, inventory, inventoryTag) -> inventory.deserializeNBT(provider, inventoryTag));
	}

}
