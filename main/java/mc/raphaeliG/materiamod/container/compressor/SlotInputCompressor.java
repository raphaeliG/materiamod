package mc.raphaeliG.materiamod.container.compressor;

import mc.raphaeliG.materiamod.objects.blocks.compressor.TileEntityCompressor;
import mc.raphaeliG.materiamod.recipes.compressor.CompressorRecipeHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotInputCompressor extends SlotItemHandler {

	public SlotInputCompressor(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		System.out.println("is slot enabled: " + (super.isItemValid(stack) && (getSlotIndex() == TileEntityCompressor.INPUT_FUEL ? CompressorRecipeHelper.isFuel(stack.getItem()) : CompressorRecipeHelper.isRecipe(stack.getItem()))));
		return  super.isItemValid(stack) && (getSlotIndex() == TileEntityCompressor.INPUT_FUEL ? CompressorRecipeHelper.isFuel(stack.getItem()) : CompressorRecipeHelper.isRecipe(stack.getItem()));
	}

}
