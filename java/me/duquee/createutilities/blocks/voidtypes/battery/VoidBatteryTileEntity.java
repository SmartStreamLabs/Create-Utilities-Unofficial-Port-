package me.duquee.createutilities.blocks.voidtypes.battery;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import me.duquee.createutilities.CreateUtilities;
import me.duquee.createutilities.CreateUtilitiesClient;
import me.duquee.createutilities.blocks.voidtypes.VoidLinkBehaviour;
import me.duquee.createutilities.voidlink.VoidLinkSlot;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidBatteryTileEntity extends SmartBlockEntity implements IHaveGoggleInformation {

	VoidLinkBehaviour link;

	public VoidBatteryTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public void createLink() {

		Triple<VoidLinkSlot, VoidLinkSlot, VoidLinkSlot> slots = VoidLinkSlot.makeSlots(
				index -> new VoidLinkSlot(index,
						state -> state.getValue(VoidBatteryBlock.FACING),
						VecHelper.voxelSpace(5.5F, 10.5F, -.001F)));

		link = new VoidLinkBehaviour(this, slots);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		createLink();
		behaviours.add(link);
	}

	private boolean hasPersistentData() {
		return level != null && !level.isClientSide;
	}

	private static VoidBatteryData getPersistentStorageData() {
		return CreateUtilities.VOID_BATTERIES_DATA;
	}

	public VoidBattery getBattery() {
		return hasPersistentData() ?
				getPersistentStorageData().computeStorageIfAbsent(link.getNetworkKey()) :
				CreateUtilitiesClient.VOID_BATTERIES.computeStorageIfAbsent(link.getNetworkKey());
	}

	@Override
	protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(tag, registries, clientPacket);
		if (clientPacket) getBattery().deserializeNBT(tag.getCompound("Battery"));
	}

	@Override
	protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
		if (clientPacket) tag.put("Battery", getBattery().serializeNBT());
		super.write(tag, registries, clientPacket);
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

		VoidBattery battery = getBattery();

		new LangBuilder(CreateUtilities.ID)
				.translate("tooltip.void_battery.header")
				.forGoggles(tooltip);

		new LangBuilder(CreateUtilities.ID)
				.translate("tooltip.void_battery.energy")
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip, 1);

		new LangBuilder(CreateUtilities.ID)
				.add(new LangBuilder(CreateUtilities.ID)
						.text(battery.getEnergyStored() + "fe")
						.style(ChatFormatting.GOLD))
				.add(new LangBuilder(CreateUtilities.ID)
						.text(" / ")
						.style(ChatFormatting.GRAY))
				.add(new LangBuilder(CreateUtilities.ID)
						.text(battery.getMaxEnergyStored() + "fe")
						.style(ChatFormatting.DARK_GRAY))
				.forGoggles(tooltip, 1);

		return true;
	}

}
