package me.duquee.createutilities.mountedstorage;

import com.mojang.serialization.Codec;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.api.contraption.storage.item.WrapperMountedItemStorage;
import me.duquee.createutilities.CUCodecs;
import me.duquee.createutilities.CreateUtilities;
import me.duquee.createutilities.blocks.voidtypes.chest.VoidChestInventory;
import me.duquee.createutilities.blocks.voidtypes.chest.VoidChestTileEntity;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class VoidChestMountedStorage extends WrapperMountedItemStorage<VoidChestInventory> {

    public static final Codec<VoidChestMountedStorage> CODEC = CUCodecs.NETWORK_KEY_CODEC
            .xmap(VoidChestMountedStorage::new, (storage) -> storage.wrapped.getKey());

    protected VoidChestMountedStorage(MountedItemStorageType<?> type, VoidChestInventory wrapped) {
        super(type, wrapped);
    }

    protected VoidChestMountedStorage(VoidChestInventory wrapped) {
        this(CUMountedStorages.VOID_CHEST.get(), wrapped);
    }

    private VoidChestMountedStorage(VoidMotorNetworkHandler.NetworkKey key) {
        this(CreateUtilities.VOID_CHEST_INVENTORIES_DATA.computeStorageIfAbsent(key));
    }

    @Override
    public void unmount(Level level, BlockState blockState, BlockPos blockPos, @Nullable BlockEntity blockEntity) {}

    @Override
    protected void playOpeningSound(ServerLevel level, Vec3 pos) {
        level.playSound(null, BlockPos.containing(pos), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void playClosingSound(ServerLevel level, Vec3 pos) {
        level.playSound(null, BlockPos.containing(pos), SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    public static VoidChestMountedStorage fromVoidChest(VoidChestTileEntity voidChest) {
        return new VoidChestMountedStorage(voidChest.getItemStorage());
    }

}