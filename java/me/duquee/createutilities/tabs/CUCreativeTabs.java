package me.duquee.createutilities.tabs;

import me.duquee.createutilities.CreateUtilities;
import me.duquee.createutilities.blocks.CUBlocks;
import me.duquee.createutilities.items.CUItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CUCreativeTabs {

	private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, CreateUtilities.ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE = TAB_REGISTER.register("base",
			() -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.createutilities.base"))
					.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
					.icon(CUBlocks.VOID_MOTOR::asStack)
					.displayItems((params, output) -> {
						output.accept(CUBlocks.VOID_STEEL_BLOCK);
						output.accept(CUItems.VOID_STEEL_INGOT);
						output.accept(CUItems.VOID_STEEL_SHEET);
						output.accept(CUBlocks.VOID_STEEL_SCAFFOLD);
						output.accept(CUBlocks.VOID_STEEL_LADDER);
						output.accept(CUBlocks.VOID_STEEL_BARS);
						output.accept(CUBlocks.VOID_CASING);
						output.accept(CUBlocks.VOID_MOTOR);
						output.accept(CUBlocks.VOID_CHEST);
						output.accept(CUBlocks.VOID_TANK);
						output.accept(CUBlocks.VOID_BATTERY);
						output.accept(CUItems.POLISHED_AMETHYST);
						output.accept(CUItems.GRAVITON_TUBE);
						output.accept(CUBlocks.GEARCUBE);
						output.accept(CUBlocks.LSHAPED_GEARBOX);
						output.accept(CUBlocks.AMETHYST_TILES);
						output.accept(CUBlocks.SMALL_AMETHYST_TILES);
					})
					.build());

	public static void register(IEventBus modEventBus) {
		TAB_REGISTER.register(modEventBus);
	}
}
