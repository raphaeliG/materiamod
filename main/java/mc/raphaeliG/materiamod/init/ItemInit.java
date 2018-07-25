package mc.raphaeliG.materiamod.init;

import java.util.ArrayList;
import java.util.List;

import mc.raphaeliG.materiamod.objects.items.*;
import net.minecraft.item.Item;

public class ItemInit {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item DEVELOPMENT_CARD = new ItemBase("development_card", CreativeTabInit.MAIN);
	public static final Item COMPRESSED_IRON = new ItemBase("compressed_iron", CreativeTabInit.MAIN);
	public static final Item AMETHYST_CRYSTAL = new ItemBase("amethyst_crystal", CreativeTabInit.MAIN);
	public static final Item COPPER_INGOT = new ItemBase("copper_ingot", CreativeTabInit.MAIN);
	
	public static final Item COPPER_AXE = new ToolAxe("copper_axe", MaterialInit.TOOL_COPPER, 9 - 1, 0.9f - 4, CreativeTabInit.MAIN, 2, 250);
}