package me.duquee.createutilities.blocks.voidtypes.battery;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import me.duquee.createutilities.blocks.CUTileEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import org.jetbrains.annotations.Nullable;

public class VoidBatteryBlock extends HorizontalDirectionalBlock implements IWrenchable, IBE<VoidBatteryTileEntity> {
	public static final MapCodec<VoidBatteryBlock> CODEC = simpleCodec(VoidBatteryBlock::new);

	public VoidBatteryBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState()
				.setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState()
				.setValue(FACING,context.getHorizontalDirection().getOpposite());
	}

	@Override
	public Class<VoidBatteryTileEntity> getBlockEntityClass() {
		return VoidBatteryTileEntity.class;
	}

	@Override
	public BlockEntityType<? extends VoidBatteryTileEntity> getBlockEntityType() {
		return CUTileEntities.VOID_BATTERY.get();
	}

}
