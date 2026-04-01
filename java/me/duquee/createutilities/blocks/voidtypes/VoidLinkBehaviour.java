package me.duquee.createutilities.blocks.voidtypes;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler.NetworkKey;
import me.duquee.createutilities.voidlink.VoidLinkSlot;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.Objects;

public class VoidLinkBehaviour extends BlockEntityBehaviour implements ClipboardCloneable {

	public static final BehaviourType<VoidLinkBehaviour> TYPE = new BehaviourType<>();

	Frequency frequencyFirst = Frequency.EMPTY;
	Frequency frequencyLast = Frequency.EMPTY;
	@Nullable
	GameProfile owner;

	VoidLinkSlot firstSlot;
	VoidLinkSlot secondSlot;
	VoidLinkSlot playerSlot;

	public VoidLinkBehaviour(SmartBlockEntity te,
							 Triple<VoidLinkSlot, VoidLinkSlot, VoidLinkSlot> slots) {
		super(te);
		firstSlot = slots.getLeft();
		secondSlot = slots.getMiddle();
		this.playerSlot = slots.getRight();
	}

	@Override
	public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(nbt, registries, clientPacket);

		putFrequency(nbt, registries, "FrequencyFirst", frequencyFirst);
		putFrequency(nbt, registries, "FrequencyLast", frequencyLast);

		if (this.owner != null) {
			CompoundTag compoundTag = new CompoundTag();
			if (owner.getId() != null)
				compoundTag.putUUID("Id", owner.getId());
			if (owner.getName() != null)
				compoundTag.putString("Name", owner.getName());
			nbt.put("Owner", compoundTag);
		}

	}

	@Override
	public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(nbt, registries, clientPacket);

		frequencyFirst = readFrequency(nbt, registries, "FrequencyFirst");
		frequencyLast = readFrequency(nbt, registries, "FrequencyLast");

		if (nbt.contains("Owner", 10)) {
			CompoundTag ownerTag = nbt.getCompound("Owner");
			owner = new GameProfile(ownerTag.contains("Id") ? ownerTag.getUUID("Id") : null,
					ownerTag.contains("Name") ? ownerTag.getString("Name") : null);
		} else {
			owner = null;
		}
	}

	public NetworkKey getNetworkKey() {
		return new NetworkKey(owner, frequencyFirst, frequencyLast);
	}

	public void setFrequency(boolean first, ItemStack stack) {

		stack = stack.copy();
		stack.setCount(1);
		ItemStack toCompare = getFrequencyStack(first);
		boolean changed = !ItemStack.isSameItemSameComponents(stack, toCompare);

		if (changed) onLeaveNetwork();

		if (first) frequencyFirst = Frequency.of(stack);
		else frequencyLast = Frequency.of(stack);

		if (!changed) return;

		blockEntity.sendData();
		onJoinNetwork();

		updateBlock();

	}

	private void updateBlock() {
		blockEntity.getLevel().blockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState().getBlock());
	}

	public boolean testHit(int index, Vec3 hit) {
		BlockState state = blockEntity.getBlockState();
		Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(blockEntity.getBlockPos()));
		return getSlot(index).testHit(blockEntity.getLevel(), blockEntity.getBlockPos(), state, localHit);
	}

	public ValueBoxTransform getSlot(int index) {
		return index < 2 ? getFrequencySlot(index == 0) : playerSlot;
	}

	public ValueBoxTransform getFrequencySlot(boolean first) {
		return first ? firstSlot : secondSlot;
	}
	public ItemStack getFrequencyStack(boolean first) {
		return first ? frequencyFirst.getStack() : frequencyLast.getStack();
	}

	public boolean canInteract(Player player) {
		return !isAdventure(player) && isOwner(player);
	}

	private boolean isAdventure(Player player) {
		return player != null && !player.mayBuild() && !player.isSpectator();
	}

	@Nullable
	public GameProfile getOwner() {
		return owner;
	}

	public void setOwner(@Nullable GameProfile owner) {
		if (!Objects.equals(this.owner, owner)) {
			onLeaveNetwork();
			this.owner = owner;
			blockEntity.sendData();
			onJoinNetwork();
			updateBlock();
		}
	}

	protected void onLeaveNetwork() {}
	protected void onJoinNetwork() {}

	public boolean isOwner(Player player) {
		return owner == null || player.getGameProfile().equals(owner);
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	@Override
	public String getClipboardKey() {
		return "Frequencies";
	}

	@Override
	public boolean writeToClipboard(HolderLookup.Provider registries, CompoundTag nbt, Direction side) {
		putFrequency(nbt, registries, "First", frequencyFirst);
		putFrequency(nbt, registries, "Last", frequencyLast);
		if (owner != null) NBTHelper.putMarker(nbt, "Owned");
		return true;
	}

	@Override
	public boolean readFromClipboard(HolderLookup.Provider registries, CompoundTag nbt, Player player, Direction side, boolean simulate) {

		if (!isOwner(player)) return false;
		if (simulate) return true;

		setFrequency(true, nbt.contains("First", Tag.TAG_COMPOUND)
				? ItemStack.parseOptional(registries, nbt.getCompound("First"))
				: ItemStack.EMPTY);
		setFrequency(false, nbt.contains("Last", Tag.TAG_COMPOUND)
				? ItemStack.parseOptional(registries, nbt.getCompound("Last"))
				: ItemStack.EMPTY);
		setOwner(nbt.contains("Owned") ? player.getGameProfile() : null);

		return true;
	}

	private static void putFrequency(CompoundTag nbt, HolderLookup.Provider registries, String key, Frequency frequency) {
		ItemStack stack = frequency.getStack();
		if (stack.isEmpty()) {
			nbt.remove(key);
			return;
		}
		nbt.put(key, stack.save(registries));
	}

	private static Frequency readFrequency(CompoundTag nbt, HolderLookup.Provider registries, String key) {
		if (!nbt.contains(key, Tag.TAG_COMPOUND)) {
			return Frequency.EMPTY;
		}
		return Frequency.of(ItemStack.parseOptional(registries, nbt.getCompound(key)));
	}
}
