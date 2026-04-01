package me.duquee.createutilities.blocks.lgearbox;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlockEntity;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.AbstractInstance;
import dev.engine_room.flywheel.lib.instance.FlatLit;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class LShapedGearboxVisual extends KineticBlockEntityVisual<GearboxBlockEntity> {

	protected final EnumMap<Direction, RotatingInstance> keys = new EnumMap<>(Direction.class);
	protected Direction sourceFacing;

	public LShapedGearboxVisual(VisualizationContext context, GearboxBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);

		updateSourceFacing();
		Instancer<RotatingInstance> instancer = this.instancerProvider()
				.instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF));

		Direction facing1 = blockState.getValue(LShapedGearboxBlock.FACING_1);
		putShaft(blockEntity, facing1);

		Direction facing2 = LShapedGearboxBlock.getAbsolute(facing1, blockState.getValue(LShapedGearboxBlock.FACING_2));
		putShaft(blockEntity, facing2);

	}

	private void putShaft(GearboxBlockEntity blockEntity, Direction direction) {

		final Direction.Axis axis = direction.getAxis();

		Instancer<RotatingInstance> instancer = this.instancerProvider()
				.instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF));
		RotatingInstance shaft = instancer.createInstance();

		shaft.setup(blockEntity, axis, blockEntity.getSpeed())
				.setPosition(this.getVisualPosition())
				.rotateToFace(Direction.SOUTH, direction)
				.setChanged();

		keys.put(direction, shaft);

	}

	protected void updateSourceFacing() {
		if (blockEntity.hasSource()) {
			BlockPos source = blockEntity.source.subtract(pos);
			sourceFacing = Direction.getNearest(source.getX(), source.getY(), source.getZ());
		} else {
			sourceFacing = null;
		}
	}

	@Override
	public void update(float partialTick) {
		updateSourceFacing();
		for (Map.Entry<Direction, RotatingInstance> key : keys.entrySet()) {
			Direction direction = key.getKey();
			Direction.Axis axis = direction.getAxis();
			updateRotation(key.getValue(), axis, blockEntity.getSpeed());
		}
	}

	private void updateRotation(RotatingInstance value, Direction.Axis axis, float speed) {
		value.setup(this.blockEntity, axis, speed).setChanged();
	}

	@Override
	public void updateLight(float partialTick) {
		this.relight(this.keys.values().toArray(FlatLit[]::new));
	}

	@Override
	public void _delete() {
		keys.values().forEach(AbstractInstance::delete);
		keys.clear();
	}

	@Override
	public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
		this.keys.values().forEach(consumer);
	}

}
