package me.duquee.createutilities.blocks;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.decoration.MetalLadderBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import me.duquee.createutilities.blocks.gearcube.GearcubeBlock;
import me.duquee.createutilities.blocks.lgearbox.LShapedGearboxBlock;
import me.duquee.createutilities.blocks.voidtypes.battery.VoidBatteryBlock;
import me.duquee.createutilities.blocks.voidtypes.chest.VoidChestBlock;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorBlock;
import me.duquee.createutilities.blocks.voidtypes.tank.VoidTankBlock;
import me.duquee.createutilities.items.CUItems;
import me.duquee.createutilities.mountedstorage.CUMountedStorages;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static me.duquee.createutilities.CreateUtilities.REGISTRATE;

public class CUBlocks {

	public static final BlockEntry<Block> VOID_STEEL_BLOCK = REGISTRATE.block("void_steel_block", Block::new)
			.initialProperties(() -> Blocks.NETHERITE_BLOCK)
			.properties(p -> p.mapColor(MapColor.COLOR_GREEN))
			.properties(p -> p.strength(55.0F, 1200.0F))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.transform(pickaxeOnly())
			.simpleItem()
			.register();

	public static final BlockEntry<MetalScaffoldingBlock> VOID_STEEL_SCAFFOLD = REGISTRATE.block("void_steel_scaffolding", MetalScaffoldingBlock::new)
			.transform(BuilderTransformers.scaffold("void_steel",
					() -> DataIngredient.items(CUItems.VOID_STEEL_INGOT.get()), MapColor.COLOR_GREEN,
					CUSpriteShifts.VOID_STEEL_SCAFFOLD, CUSpriteShifts.VOID_STEEL_SCAFFOLD_INSIDE, CUSpriteShifts.VOID_CASING))
			.register();

	public static final BlockEntry<MetalLadderBlock> VOID_STEEL_LADDER = REGISTRATE.block("void_steel_ladder", MetalLadderBlock::new)
			.transform(BuilderTransformers.ladder("void_steel",
					() -> DataIngredient.items(CUItems.VOID_STEEL_INGOT.get()), MapColor.COLOR_GREEN))
			.register();

	public static final BlockEntry<IronBarsBlock> VOID_STEEL_BARS = REGISTRATE.block("void_steel_bars", IronBarsBlock::new)
			.addLayer(() -> RenderType::cutoutMipped)
			.initialProperties(() -> Blocks.IRON_BARS)
			.properties(p -> p.sound(SoundType.COPPER).mapColor(MapColor.COLOR_GREEN))
			.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
			.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
			.transform(pickaxeOnly())
			.item()
			.build()
			.register();

	public static final BlockEntry<CasingBlock> VOID_CASING = REGISTRATE.block("void_casing", CasingBlock::new)
			.transform(BuilderTransformers.casing(() -> CUSpriteShifts.VOID_CASING))
			.properties(p -> p.mapColor(MapColor.COLOR_BLACK))
			.properties(p -> p.strength(55.0F, 1200.0F))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.transform(pickaxeOnly())
			.register();

	public static final BlockEntry<VoidMotorBlock> VOID_MOTOR = REGISTRATE.block("void_motor", VoidMotorBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.mapColor(MapColor.COLOR_BLACK))
			.properties(p -> p.strength(30F, 600.0F))
			.transform(pickaxeOnly())
			//.transform(CStress.setNoImpact())
			.item()
			.properties(p -> p.rarity(Rarity.EPIC))
			.transform(customItemModel())
			.register();

	public static final BlockEntry<VoidChestBlock> VOID_CHEST = REGISTRATE.block("void_chest", VoidChestBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.mapColor(MapColor.COLOR_BLACK))
			.properties(p -> p.strength(30F, 600.0F))
			.transform(pickaxeOnly())
			.transform(MountedItemStorageType.mountedItemStorage(CUMountedStorages.VOID_CHEST))
			.item()
			.properties(p -> p.rarity(Rarity.EPIC))
			.transform(customItemModel())
			.register();

	public static final BlockEntry<VoidTankBlock> VOID_TANK = REGISTRATE.block("void_tank", VoidTankBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.mapColor(MapColor.COLOR_BLACK))
			.properties(p -> p.strength(30F, 600.0F))
			.properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
			.transform(pickaxeOnly())
			.item()
			.properties(p -> p.rarity(Rarity.EPIC))
			.transform(customItemModel())
			.register();

	public static final BlockEntry<VoidBatteryBlock> VOID_BATTERY = REGISTRATE.block("void_battery", VoidBatteryBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.mapColor(MapColor.COLOR_BLACK))
			.properties(p -> p.strength(30F, 600.0F))
			.transform(pickaxeOnly())
			.item()
			.properties(p -> p.rarity(Rarity.EPIC))
			.transform(customItemModel())
			.register();

	public static final BlockEntry<GearcubeBlock> GEARCUBE = REGISTRATE.block("gearcube", GearcubeBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.mapColor(MapColor.PODZOL))
			//.transform(CStress.setNoImpact())
			.transform(axeOrPickaxe())
			.simpleItem()
			.register();

	public static final BlockEntry<LShapedGearboxBlock> LSHAPED_GEARBOX = REGISTRATE.block("lshaped_gearbox", LShapedGearboxBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.mapColor(MapColor.PODZOL))
			//.transform(CStress.setNoImpact())
			.transform(axeOrPickaxe())
			.onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.ANDESITE_CASING)))
			.onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.ANDESITE_CASING,
					(state, face) -> !LShapedGearboxBlock.hasShaftTowards(state, face))))
			.simpleItem()
			.register();

	public static final BlockEntry<Block> AMETHYST_TILES = REGISTRATE.block("amethyst_tiles", Block::new)
			.initialProperties(() -> Blocks.DEEPSLATE)
			.properties(p -> p.mapColor(MapColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops())
			.properties(p -> p.sound(SoundType.AMETHYST_CLUSTER))
			.transform(pickaxeOnly())
			.simpleItem()
			.register();

	public static final BlockEntry<Block> SMALL_AMETHYST_TILES = REGISTRATE.block("small_amethyst_tiles", Block::new)
			.initialProperties(() -> Blocks.DEEPSLATE)
			.properties(p -> p.mapColor(MapColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops())
			.properties(p -> p.sound(SoundType.AMETHYST_CLUSTER))
			.transform(pickaxeOnly())
			.simpleItem()
			.register();

	public static void register() {}

}
