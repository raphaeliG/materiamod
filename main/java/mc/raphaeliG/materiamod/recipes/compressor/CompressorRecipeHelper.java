package mc.raphaeliG.materiamod.recipes.compressor;

import java.util.ArrayList;

import mc.raphaeliG.materiamod.init.ItemInit;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CompressorRecipeHelper {
	public static final ArrayList<CompressorRecipe> recipes = new ArrayList<CompressorRecipe>();
	public static final ArrayList<CompressorFuel> fuels = new ArrayList<CompressorFuel>();
	//recipes
	private static final CompressorRecipe COMPRESSED_IRON = new CompressorRecipe(Items.IRON_INGOT, ItemInit.COMPRESSED_IRON, 100);
	//fuels
	private static final CompressorFuel REDSTONE_DUST = new CompressorFuel(Items.REDSTONE, 800);

	public static boolean isFuel(Item item) {
		System.out.println("Is a fuel: " + (getFuelTime(new ItemStack(item)) != 0));
		return getFuelTime(new ItemStack(item)) != 0;
	}
	
	public static int getFuelTime(ItemStack itemstack ) {
		for (CompressorFuel i : fuels)
		{
			if (itemstack.getItem() == i.getFuel()) return i.getTimeInTicks(); //use this
		}
		return 0;
	}

	public static boolean isRecipe(Item item) {
		System.out.println("Is a recipe: " + (getOutput(item) != null));
		return getOutput(item) != null;
	}
	
	public static ItemStack getOutput(Item input)
	{
		for (CompressorRecipe i : recipes)
		{
			if (i.getInput() == input) return new ItemStack(i.getOutput());
		}
		return ItemStack.EMPTY;
		
	}
	
	public static int getRecipeTime(Item input)
	{
		for (CompressorRecipe i : recipes)
		{
			if (i.getInput() == input) return i.getTimeInTicks();
		}
		return 0;
		
	}
}
