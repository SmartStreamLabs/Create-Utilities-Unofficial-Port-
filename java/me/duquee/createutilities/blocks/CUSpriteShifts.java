package me.duquee.createutilities.blocks;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

import me.duquee.createutilities.CreateUtilities;

public class CUSpriteShifts {

	public static final CTSpriteShiftEntry
			VOID_CASING = omni("void_casing"),
			VOID_STEEL_SCAFFOLD = horizontal("scaffold/void_steel_scaffold"),
			VOID_STEEL_SCAFFOLD_INSIDE = horizontal("scaffold/void_steel_scaffold_inside");

	private static CTSpriteShiftEntry omni(String name) {
		return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
	}

	private static CTSpriteShiftEntry horizontal(String name) {
		return getCT(AllCTTypes.HORIZONTAL, name);
	}

	private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
		return getCT(type, blockTextureName, blockTextureName);
	}

	private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
		return CTSpriteShifter.getCT(type, CreateUtilities.asResource("block/" + blockTextureName), CreateUtilities.asResource("block/" + connectedTextureName + "_connected"));
	}

}
