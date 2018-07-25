package mc.raphaeliG.materiamod.recipes.compressor;

import net.minecraft.item.Item;

public class CompressorRecipe {
	
	private Item input;
	private Item output;
	private int timeInTicks;
	
	public CompressorRecipe(Item input, Item output, int timeInTicks) {
		this.input = input;
		this.output = output;
		this.timeInTicks = timeInTicks;
		
		CompressorRecipeHelper.recipes.add(this);
	}
	
	public Item getInput() {
		return input;
	}
	
	public Item getOutput() {
		return output;
	}
	
	public int getTimeInTicks() {
		return timeInTicks;
	}
}
