package me.duquee.createutilities.blocks.voidtypes.motor;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.levelWrappers.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.*;

public class VoidMotorNetworkHandler {

	static final Map<LevelAccessor, Map<NetworkKey, Set<BlockPos>>> connections =
			new IdentityHashMap<>();

	public Set<BlockPos> getNetworkOf(LevelAccessor world, VoidMotorLinkBehaviour actor) {
		Map<NetworkKey, Set<BlockPos>> networksInWorld = networksIn(world);
		NetworkKey key = actor.getNetworkKey();
		if (!networksInWorld.containsKey(key))
			networksInWorld.put(key, new LinkedHashSet<>());
		return networksInWorld.get(key);
	}

	public Map<NetworkKey, Set<BlockPos>> networksIn(LevelAccessor world) {
		if (!connections.containsKey(world)) {
			Create.LOGGER.warn("Tried to Access unprepared network space of " + WorldHelper.getDimensionID(world));
			return new HashMap<>();
		}
		return connections.get(world);
	}

	public void onLoadWorld(LevelAccessor world) {
		connections.put(world, new HashMap<>());
		Create.LOGGER.debug("Prepared Void Motor Network Space for " + WorldHelper.getDimensionID(world));
	}

	public void onUnloadWorld(LevelAccessor world) {
		connections.remove(world);
		Create.LOGGER.debug("Removed Void Motor Network Space for " + WorldHelper.getDimensionID(world));
	}

	public void addToNetwork(LevelAccessor world, VoidMotorLinkBehaviour actor) {
		getNetworkOf(world, actor).add(actor.getPos());
		if (actor.blockEntity instanceof VoidMotorTileEntity voidMotor) voidMotor.onConnectToVoidNetwork();
	}

	public void removeFromNetwork(LevelAccessor world, VoidMotorLinkBehaviour actor) {
		if (actor.blockEntity instanceof VoidMotorTileEntity voidMotor) voidMotor.onDisconnectFromVoidNetwork();
		Set<BlockPos> network = getNetworkOf(world, actor);
		network.remove(actor.getPos());
		if (network.isEmpty()) networksIn(world).remove(actor.getNetworkKey());
	}

	public static class NetworkKey {

		@Nullable
		public final GameProfile owner;
		public final Couple<Frequency> frequencies;

		public NetworkKey(@Nullable GameProfile owner, Frequency frequencyFirst, Frequency frequencySecond) {
			this.owner = owner;
			this.frequencies = Couple.create(frequencyFirst, frequencySecond);
		}

		public void writeToBuffer(RegistryFriendlyByteBuf buffer) {
			buffer.writeNbt(serialize(buffer.registryAccess()));
		}

		public static NetworkKey fromBuffer(RegistryFriendlyByteBuf buffer) {
			return deserialize(buffer.registryAccess(), buffer.readNbt());
		}

		@Override
		public int hashCode() {
			return Objects.hash(owner, frequencies);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			NetworkKey other = (NetworkKey) obj;
			return Objects.equals(owner, other.owner) && frequencies.equals(other.frequencies);
		}

		public CompoundTag serialize(HolderLookup.Provider registries) {
			CompoundTag tag = new CompoundTag();
			if (owner != null) {
				if (owner.getId() != null)
					tag.putUUID("OwnerId", owner.getId());
				if (owner.getName() != null)
					tag.putString("OwnerName", owner.getName());
			}
			putFrequency(tag, registries, "FrequencyFirst", frequencies.get(true));
			putFrequency(tag, registries, "FrequencyLast", frequencies.get(false));
			return tag;
		}

		public static NetworkKey deserialize(HolderLookup.Provider registries, CompoundTag tag) {
			Frequency frequencyFirst = readFrequency(tag, registries, "FrequencyFirst");
			Frequency frequencyLast = readFrequency(tag, registries, "FrequencyLast");
			GameProfile owner = null;
			if (tag.contains("OwnerId") || tag.contains("OwnerName"))
				owner = new GameProfile(tag.contains("OwnerId") ? tag.getUUID("OwnerId") : null,
						tag.contains("OwnerName") ? tag.getString("OwnerName") : null);
			return new NetworkKey(owner, frequencyFirst, frequencyLast);
		}

		private static void putFrequency(CompoundTag tag, HolderLookup.Provider registries, String key, Frequency frequency) {
			ItemStack stack = frequency.getStack();
			if (stack.isEmpty()) {
				tag.remove(key);
				return;
			}
			tag.put(key, stack.save(registries));
		}

		private static Frequency readFrequency(CompoundTag tag, HolderLookup.Provider registries, String key) {
			if (!tag.contains(key, Tag.TAG_COMPOUND)) {
				return Frequency.EMPTY;
			}
			return Frequency.of(ItemStack.parseOptional(registries, tag.getCompound(key)));
		}

	}

}
