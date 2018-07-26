package mc.raphaeliG.materiamod.container.compressor;

import mc.raphaeliG.materiamod.tileentity.TileEntityCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCompressor extends Container{
	
	private TileEntityCompressor tileEntity;
	private IItemHandler handler;
	
    private int compressTime;
    private int totalCompressTime;
    private int fuelTime;
    private int currentItemFuelTime;
	
	public ContainerCompressor(IInventory playerInventory, TileEntityCompressor tileEntity) {
		this.tileEntity = tileEntity;
		handler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotInputFuel(handler, TileEntityCompressor.INPUT_FUEL, 20, 35));
		addSlotToContainer(new SlotInputRcipe(handler, TileEntityCompressor.INPUT_TO_COMPRESS, 56, 35));
		addSlotToContainer(new SlotOutput(handler, TileEntityCompressor.OUTPUT, 116, 35));
		
		
		//players inventory in gui
		int xPos = 8;
		int yPos = 84;
				
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}
				
		for (int x = 0; x < 9; ++x) {
			addSlotToContainer(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
	    ItemStack previous = ItemStack.EMPTY;
	    Slot slot = (Slot) inventorySlots.get(fromSlot);
	    if (slot != null && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();
	        if (fromSlot < handler.getSlots()) {
	            // From the block breaker inventory to player's inventory
	            if (!mergeItemStack(current, handler.getSlots(), handler.getSlots() + 36, true))
	                return ItemStack.EMPTY;
	        } else {
	            // From the player's inventory to block breaker's inventory
	            if (!this.mergeItemStack(current, 0, handler.getSlots(), false))
	                return ItemStack.EMPTY;
	        }
	        if (current.getCount() == 0) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
	            slot.putStack(ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
	        else
	            slot.onSlotChanged();
	        if (current.getCount() == previous.getCount())
	            return null;
	        slot.onTake(playerIn, current);
	    }
	    return previous;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !playerIn.isSpectator();
	}
}
