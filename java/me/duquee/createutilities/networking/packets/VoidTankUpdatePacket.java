package me.duquee.createutilities.networking.packets;

import me.duquee.createutilities.CreateUtilities;
import me.duquee.createutilities.CreateUtilitiesClient;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler.NetworkKey;
import me.duquee.createutilities.blocks.voidtypes.tank.VoidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class VoidTankUpdatePacket implements CustomPacketPayload {

	public static final Type<VoidTankUpdatePacket> TYPE = new Type<>(CreateUtilities.asResource("void_tank_update"));
	public static final StreamCodec<RegistryFriendlyByteBuf, VoidTankUpdatePacket> STREAM_CODEC =
			StreamCodec.ofMember(VoidTankUpdatePacket::write, VoidTankUpdatePacket::new);

	private final NetworkKey key;
	private final CompoundTag tankTag;

	public VoidTankUpdatePacket(NetworkKey key, VoidTank tank, HolderLookup.Provider registries) {
		this(key, tank.writeToNBT(registries, new CompoundTag()));
	}

	private VoidTankUpdatePacket(NetworkKey key, CompoundTag tankTag) {
		this.key = key;
		this.tankTag = tankTag;
	}

	private VoidTankUpdatePacket(RegistryFriendlyByteBuf buffer) {
		this(NetworkKey.fromBuffer(buffer), buffer.readNbt());
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		key.writeToBuffer(buffer);
		buffer.writeNbt(tankTag);
	}

	public static void handle(VoidTankUpdatePacket packet, IPayloadContext context) {
		VoidTank tank = new VoidTank(packet.key);
		tank.readFromNBT(Minecraft.getInstance().level.registryAccess(), packet.tankTag);
		CreateUtilitiesClient.VOID_TANKS.storages.put(packet.key, tank);
	}

	@Override
	public Type<VoidTankUpdatePacket> type() {
		return TYPE;
	}
}
