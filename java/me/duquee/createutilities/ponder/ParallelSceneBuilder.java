package me.duquee.createutilities.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.foundation.instruction.PonderInstruction;
import org.jetbrains.annotations.NotNull;

public class ParallelSceneBuilder extends CreateSceneBuilder {

	private final ParallelInstruction instruction;

	public ParallelSceneBuilder(SceneBuilder ponderScene, ParallelInstruction instruction) {
		super(ponderScene);
		this.instruction = instruction;
	}

	@Override
	public void addInstruction(@NotNull PonderInstruction instruction) {
		this.instruction.addInstruction(instruction);
	}

}
