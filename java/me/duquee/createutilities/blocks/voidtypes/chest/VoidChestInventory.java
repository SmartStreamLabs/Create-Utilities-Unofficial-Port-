package me.duquee.createutilities.blocks.voidtypes.chest;

import me.duquee.createutilities.CreateUtilities;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler.NetworkKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class VoidChestInventory extends ItemStackHandler {

	private final NetworkKey key;

	public VoidChestInventory(NetworkKey key) {
		super(27);
		this.key = key;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (CreateUtilities.VOID_CHEST_INVENTORIES_DATA != null) CreateUtilities.VOID_CHEST_INVENTORIES_DATA.setDirty();
	}

	public boolean isEmpty() {
		return stacks.stream().allMatch(ItemStack::isEmpty);
	}

	public NetworkKey getKey() {
		return key;
	}

}
