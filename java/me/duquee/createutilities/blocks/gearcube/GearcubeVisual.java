package me.duquee.createutilities.blocks.gearcube;

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
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class GearcubeVisual extends KineticBlockEntityVisual<GearboxBlockEntity> {

	protected final EnumMap<Direction, RotatingInstance> keys = new EnumMap<>(Direction.class);
	protected Direction sourceFacing;

	public GearcubeVisual(VisualizationContext context, GearboxBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);

		this.updateSourceFacing();
		Instancer<RotatingInstance> instancer = this.instancerProvider()
				.instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF));

		for (Direction direction : Iterate.directions) {

			final Direction.Axis axis = direction.getAxis();
			RotatingInstance shaft = instancer.createInstance();
			shaft.setup(blockEntity, axis, this.getSpeed(direction))
					.setPosition(this.getVisualPosition())
					.rotateToFace(Direction.SOUTH, direction)
					.setChanged();

			keys.put(direction, shaft);
		}
	}

	private float getSpeed(Direction direction) {
		float speed = blockEntity.getSpeed();

		if (speed != 0 && sourceFacing != null) {
			if (sourceFacing.getAxis() == direction.getAxis())
				speed *= sourceFacing == direction ? 1 : -1;
			else if (sourceFacing.getAxisDirection() == direction.getAxisDirection())
				speed *= -1;
		}
		return speed;
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
			updateRotation(key.getValue(), axis, getSpeed(direction));
		}
	}

	private void updateRotation(RotatingInstance value, Direction.Axis axis, float speed) {
		value.setup(this.blockEntity, axis, speed).setChanged();
	}

	@Override
	public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
		this.keys.values().forEach(consumer);
	}

	@Override
	public void updateLight(float partialTick) {
		this.relight(this.keys.values().toArray(FlatLit[]::new));
	}

	@Override
	protected void _delete() {
		keys.values().forEach(AbstractInstance::delete);
		keys.clear();
	}
}
