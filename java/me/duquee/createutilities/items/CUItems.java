package me.duquee.createutilities.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static me.duquee.createutilities.CreateUtilities.REGISTRATE;

public class CUItems {

	public static final ItemEntry<Item> VOID_STEEL_INGOT = ingredient("void_steel_ingot");
	public static final ItemEntry<Item> VOID_STEEL_SHEET = ingredient("void_steel_sheet");
	public static final ItemEntry<Item> POLISHED_AMETHYST = ingredient("polished_amethyst");
	public static final ItemEntry<Item> GRAVITON_TUBE = ingredient("graviton_tube");

	private static ItemEntry<Item> ingredient(String name) {
		return REGISTRATE.item(name, Item::new)
				.register();
	}

	public static void register() {}

}
