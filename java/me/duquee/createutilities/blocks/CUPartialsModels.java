package me.duquee.createutilities.blocks;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import me.duquee.createutilities.CreateUtilities;

public class CUPartialsModels {

	public static final PartialModel VOID_CHEST_LID = block("void_chest/lid");

	public static final PartialModel VOID_BATTERY_DIAL = block("void_battery/dial");

	private static PartialModel block(String path) {
		return PartialModel.of(CreateUtilities.asResource("block/" + path));
	}

	public static void init() {}

}
