package mc.raphaeliG.materiamod.tileentity;

import mc.raphaeliG.materiamod.recipes.compressor.CompressorRecipeHelper;
import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCompressor extends TileEntity implements ITickable, ICapabilityProvider{

	public static final int INPUT_FUEL = 0;
	public static final int INPUT_TO_COMPRESS = 1;
	public static final int OUTPUT = 2;
	
	//The number of ticks that the compressor will keep compressing
	public int furnaceBurnTime = 0;
	//The number of ticks that a fresh copy of the currently-used fuel item would keep the compressor compressing for
	public int currentItemBurnTime = 0;
    //The number of ticks the compressor is compressing
	public int cookTime = 0;
    //The number of ticks that a fresh copy of the currently-compressed item would need to be compressed for
	public int totalCookTime = 0;
	
	private ItemStackHandler handler;
	
	public TileEntityCompressor() {
		handler = new ItemStackHandler(3);
	}
	
	private boolean isCompressing()
	{
		return furnaceBurnTime > 0;
	}
	
	@Override
	public void update() {
		boolean flag = isCompressing();
		if (!world.isRemote)
		{
			if(isCompressing())
			{
				--furnaceBurnTime;
				if (canCompress())
				{
					progress();
				}
			}
			else if (!handler.getStackInSlot(INPUT_FUEL).isEmpty() && canCompress())
			{
				totalCookTime = CompressorRecipeHelper.getRecipeTime(handler.getStackInSlot(INPUT_TO_COMPRESS).getItem());
				cookTime = 0;
				currentItemBurnTime = CompressorRecipeHelper.getFuelTime(handler.getStackInSlot(INPUT_FUEL));
				furnaceBurnTime = currentItemBurnTime;
				handler.getStackInSlot(INPUT_FUEL).shrink(1);
				progress();
			}
			else
			{
				currentItemBurnTime = 0;
				cookTime = 0;
				totalCookTime = 0;
			}
		}
		if (flag != isCompressing())
		{
			//Change state
		}
	}
	
	private void progress() {
		++cookTime;
		if (cookTime == totalCookTime)
		{
			cookTime = 0;
			if (handler.getStackInSlot(OUTPUT).isEmpty())
			{
				handler.setStackInSlot(OUTPUT, new ItemStack(CompressorRecipeHelper.getOutput(handler.getStackInSlot(INPUT_TO_COMPRESS).getItem()).getItem()));
			}
			else
			{
				handler.setStackInSlot(OUTPUT, new ItemStack(handler.getStackInSlot(OUTPUT).getItem(), handler.getStackInSlot(OUTPUT).getCount() + 1));
			}
			handler.setStackInSlot(INPUT_TO_COMPRESS, new ItemStack(handler.getStackInSlot(INPUT_TO_COMPRESS).getItem(), handler.getStackInSlot(INPUT_TO_COMPRESS).getCount() - 1));
		}
	}

	private boolean canCompress() {
		if (handler.getStackInSlot(INPUT_TO_COMPRESS).isEmpty()) return false;
		else if (handler.getStackInSlot(OUTPUT).isEmpty()) return true;
		else if (handler.getStackInSlot(OUTPUT).getItem() != CompressorRecipeHelper.getOutput(handler.getStackInSlot(INPUT_TO_COMPRESS).getItem()).getItem()) return false;
		else if (handler.getStackInSlot(OUTPUT).getCount() + 1 <= 64) return true;
		else return false;
	}
	
	

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)handler;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		handler.deserializeNBT(compound.getCompoundTag("Inventory"));
		cookTime = compound.getInteger("CookTime");
		totalCookTime = compound.getInteger("TotalCookTime");
		furnaceBurnTime = compound.getInteger("BurnTime");
		currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		//TODO: writeNBT
		compound.setTag("Inventory", handler.serializeNBT());
		compound.setInteger("CookTime", cookTime);
		compound.setInteger("TotalCookTime", totalCookTime);
		compound.setInteger("BurnTime", furnaceBurnTime);
		compound.setInteger("CurrentItemBurnTime", currentItemBurnTime);
		return compound;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(pos, metadata, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getTileData() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}
}
