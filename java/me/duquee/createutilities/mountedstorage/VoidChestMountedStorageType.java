package me.duquee.createutilities.mountedstorage;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import me.duquee.createutilities.blocks.voidtypes.chest.VoidChestTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class VoidChestMountedStorageType extends MountedItemStorageType<VoidChestMountedStorage> {

    public VoidChestMountedStorageType() {
        super(VoidChestMountedStorage.CODEC.fieldOf("data"));
    }

    public @Nullable VoidChestMountedStorage mount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
        if (be instanceof VoidChestTileEntity voidChest) {
            return VoidChestMountedStorage.fromVoidChest(voidChest);
        } else return null;
    }

}
