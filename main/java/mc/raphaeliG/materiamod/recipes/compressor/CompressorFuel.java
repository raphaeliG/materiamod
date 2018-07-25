package mc.raphaeliG.materiamod.recipes.compressor;

import net.minecraft.item.Item;

public class CompressorFuel {
	
	private Item fuel;
	private int timeInTicks;
	
	public CompressorFuel(Item fuel, int timeInTicks) {
		this.fuel  = fuel;
		this.timeInTicks = timeInTicks;
		
		CompressorRecipeHelper.fuels.add(this);
	}
	
	public Item getFuel() {
		return fuel;
	}
	
	public int getTimeInTicks() {
		return timeInTicks;
	}
}
