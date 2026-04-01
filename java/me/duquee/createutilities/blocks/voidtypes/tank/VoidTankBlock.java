package me.duquee.createutilities.blocks.voidtypes.tank;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.mojang.serialization.MapCodec;
import me.duquee.createutilities.blocks.CUTileEntities;
import me.duquee.createutilities.blocks.voidtypes.VoidLinkBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class VoidTankBlock extends Block implements IWrenchable, IBE<VoidTankTileEntity> {
	public static final MapCodec<VoidTankBlock> CODEC = simpleCodec(VoidTankBlock::new);

	public static final BooleanProperty CLOSED = BooleanProperty.create("closed");

	public VoidTankBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(CLOSED, false));
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CLOSED);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (worldIn.isClientSide()) return;
		VoidLinkBehaviour behaviour = BlockEntityBehaviour.get(worldIn, pos, VoidLinkBehaviour.TYPE);
		if (placer instanceof Player player) behaviour.setOwner(player.getGameProfile());
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		context.getLevel().setBlockAndUpdate(context.getClickedPos(), state.setValue(CLOSED, !state.getValue(CLOSED)));
		return InteractionResult.SUCCESS;
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

		if (heldItem.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!player.isCreative()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

		FluidHelper.FluidExchange exchange = null;
		if (!(world.getBlockEntity(pos) instanceof VoidTankTileEntity te)) return ItemInteractionResult.FAIL;

		FluidTank fluidTank = te.getFluidStorage();
		if (fluidTank == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

		if (FluidHelper.tryEmptyItemIntoBE(world, player, hand, heldItem, te)){
			exchange = FluidHelper.FluidExchange.ITEM_TO_TANK;
		} else if (FluidHelper.tryFillItemFromBE(world, player, hand, heldItem, te))
			exchange = FluidHelper.FluidExchange.TANK_TO_ITEM;

		if (exchange == null) {
			if (GenericItemEmptying.canItemBeEmptied(world, heldItem) || GenericItemFilling.canItemBeFilled(world, heldItem))
				return ItemInteractionResult.SUCCESS;
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public Class<VoidTankTileEntity> getBlockEntityClass() {
		return VoidTankTileEntity.class;
	}

	@Override
	public BlockEntityType<? extends VoidTankTileEntity> getBlockEntityType() {
		return CUTileEntities.VOID_TANK.get();
	}

}
