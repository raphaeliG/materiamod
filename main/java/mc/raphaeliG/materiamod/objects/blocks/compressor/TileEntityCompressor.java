package mc.raphaeliG.materiamod.objects.blocks.compressor;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
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
	
	private boolean burns = false;
	
	//The number of ticks that the compressor will keep compressing
	private int furnaceBurnTime = 0;
	//The number of ticks that a fresh copy of the currently-used fuel item would keep the compressor compressing for
    private int currentItemBurnTime = 0;
    //The number of ticks the compressor is compressing
    private int cookTime = 0;
    //The number of ticks that a fresh copy of the currently-compressed item would need to be compressed for
    private int totalCookTime = 0;
	
	private ItemStackHandler handler;
	
	public TileEntityCompressor() {
		handler = new ItemStackHandler(3);
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
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		//TODO: writeNBT
		compound.setTag("Inventory", handler.serializeNBT());
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
