package mc.raphaeliG.materiamod.container.compressor;

import mc.raphaeliG.materiamod.recipes.compressor.CompressorRecipeHelper;
import mc.raphaeliG.materiamod.tileentity.TileEntityCompressor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotInputFuel extends SlotItemHandler {

	public SlotInputFuel(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		
		return  super.isItemValid(stack) && CompressorRecipeHelper.isFuel(stack.getItem());
	}

}
