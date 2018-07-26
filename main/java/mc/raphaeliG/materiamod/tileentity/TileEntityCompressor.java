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
	
	private boolean burns = false;
	
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
	
	public boolean isBurning()
    {
        return furnaceBurnTime > 0;
    }
    
    public void update()
    {
        boolean flag = isBurning();
        boolean flag1 = false;

        if (isBurning())
        {
            --furnaceBurnTime;
        }

        if (!world.isRemote)
        {
            ItemStack fuel = handler.getStackInSlot(INPUT_FUEL);

            if (isBurning() || !fuel.isEmpty() && !handler.getStackInSlot(INPUT_TO_COMPRESS).isEmpty())//if already compressing or there's fuel in slot and there's something to compress 
            {
                if (!isBurning() && canSmelt())//if isn't compressing but can smelt
                {
                    furnaceBurnTime = CompressorRecipeHelper.getFuelTime(fuel);
                    currentItemBurnTime = furnaceBurnTime;

                    if (isBurning())//if now it is burning
                    {
                        flag1 = true;//STATE CHANGE

                        if (!fuel.isEmpty())//if fuel slot isn't empty
                        {
                            Item item = fuel.getItem();
                            fuel.shrink(1);//shrink fuel slot

                            if (fuel.isEmpty())//if now fuel slot is empty
                            {
                                ItemStack item1 = item.getContainerItem(fuel);
                                handler.setStackInSlot(INPUT_FUEL, item1);
                            }
                        }
                    }
                }

                if (isBurning() && canSmelt())//if compressing and can compress
                {
                    ++cookTime;

                    if (cookTime == totalCookTime)//if done smelting
                    {
                        cookTime = 0;
                        totalCookTime = CompressorRecipeHelper.getRecipeTime(handler.getStackInSlot(INPUT_TO_COMPRESS).getItem());
                        smeltItem();
                        flag1 = true;
                    }
                }
                else//if can't compress, reset progress
                {
                    cookTime = 0;
                }
            }
            else if (!isBurning() && cookTime > 0)//if isn't compressing but compressTime is more than zero
            {
                cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);
            }

            if (flag != isBurning())//if the compressor state has changed
            {
                flag1 = true;
                //BlockFurnace.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (flag1)//if compressor state has changed
        {
            markDirty();
        }
    }
    
    private boolean canSmelt()
    {
        if (handler.getStackInSlot(INPUT_TO_COMPRESS).isEmpty())//if there isn't anything to compress, return FALSE
        {
            return false;
        }
        else//if there is something compress
        {
            ItemStack result = CompressorRecipeHelper.getOutput(handler.getStackInSlot(INPUT_TO_COMPRESS).getItem());

            if (result.isEmpty())//if there is no result, return FALSE
            {
                return false;
            }
            else//if there is a result
            {
                ItemStack outputStack = handler.getStackInSlot(OUTPUT);

                if (outputStack.isEmpty())//if there is nothing in the output stack, return TRUE
                {
                    return true;
                }
                else if (!outputStack.isItemEqual(result))//if there is something in the output stack, but it's different than the result, return FALSE
                {
                    return false;
                }
                else if (outputStack.getCount() + result.getCount() <= handler.getSlotLimit(OUTPUT) && outputStack.getCount() + result.getCount() <= outputStack.getMaxStackSize())//if there is enough space for the result stack, return TRUE
                {
                    return true;
                }
                else
                {
                    return outputStack.getCount() + result.getCount() <= result.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }
    
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack input = handler.getStackInSlot(INPUT_TO_COMPRESS);
            ItemStack result = CompressorRecipeHelper.getOutput(input.getItem());
            ItemStack output = handler.getStackInSlot(OUTPUT);

            if (output.isEmpty())
            {
                handler.setStackInSlot(OUTPUT, result.copy());
            }
            else if (output.getItem() == result.getItem())
            {
            	output.grow(result.getCount());
            }
            input.shrink(1);
        }
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
