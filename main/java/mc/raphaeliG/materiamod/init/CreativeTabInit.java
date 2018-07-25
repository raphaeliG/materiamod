package mc.raphaeliG.materiamod.init;

import mc.raphaeliG.materiamod.util.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabInit {
	public static final CreativeTabs MAIN = new CreativeTabs("main"){public ItemStack getTabIconItem(){return new ItemStack(ItemInit.DEVELOPMENT_CARD);}};
	public static final CreativeTabs MACHINES = new CreativeTabs("machines"){public ItemStack getTabIconItem(){return new ItemStack(Item.getItemFromBlock(BlockInit.SINTERING_FURNACE));}};
}
