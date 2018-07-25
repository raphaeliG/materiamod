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
	    public void update()
	    {
	        boolean flag = this.isCondensing();
	        boolean flag1 = false;

	        if (this.isCondensing()) {
	            --this.condensingTime;
	        }

	        if (!this.world.isRemote) {
	            ItemStack input1 = this.condenserItemStacks.get(0);
	            ItemStack input2 = this.condenserItemStacks.get(1);
	            ItemStack input3 = this.condenserItemStacks.get(2);
	            ItemStack input4 = this.condenserItemStacks.get(3);

	            if (this.isCondensing() || !input1.isEmpty() && !input2.isEmpty() && !input3.isEmpty() && !input4.isEmpty()) {
	                if (!this.isCondensing() && this.canCondense()) {
	                    this.condensingTime = this.getCondenseTime();

	                    if (this.isCondensing()) {
	                        flag1 = true;
	                    }
	                }

	                if (this.isCondensing() && this.canCondense())
	                {
	                    ++this.condenseTime;

	                    if (this.condenseTime == this.totalCondensingTime)
	                    {
	                        this.condenseTime = 0;
	                        this.totalCondensingTime = this.getCondenseTime();
	                        this.condenseItem();
	                        flag1 = true;
	                    }
	                }
	                else
	                {
	                    this.condenseTime = 0;
	                }
	            }
	            else if (!this.isCondensing() && this.condenseTime > 0)
	            {
	                this.condenseTime = MathHelper.clamp(this.condenseTime - 2, 0, this.totalCondensingTime);
	            }

	            if (flag != this.isCondensing())
	            {
	                flag1 = true;
	                BlockCondenser.setState(this.isCondensing(), this.world, this.pos);
	            }
	        }

	        if (flag1)
	        {
	            this.markDirty();
	        }
	    }

	    public int getCondenseTime()
	    {
	    	int time = 600;
	    	Item item = this.condenserItemStacks.get(5).getItem();
	    	if(isItemUpgrade()) {
	    		if(item == ItemInit.UPGRADE_SPEED_1) {
	        		return (int)((float)time * 0.75);
	        	} else if(item == ItemInit.UPGRADE_SPEED_2) {
	        		return (int)((float)time * 0.5);
	        	} else if(item == ItemInit.UPGRADE_SPEED_3) {
	        		return (int)((float)time * 0.25);
	        	} else {
	        		return time;
	        	}
	    	}
	        return time;
	    }
	    
	    public int upgradeOutput(int amount) {
	    	Item item = this.condenserItemStacks.get(3).getItem();
	    	if(isItemUpgrade()) {
	    		if(item == ItemInit.UPGRADE_DOUBLE_1) {
	        		return amount + (int)((float)amount * 0.3);
	        	} else if(item == ItemInit.UPGRADE_DOUBLE_2) {
	        		return amount + (int)((float)amount * 0.6);
	        	} else if(item == ItemInit.UPGRADE_DOUBLE_3) {
	        		return amount + (int)((float)amount * 0.9);
	        	} else {
	        		return amount;
	        	}
	    	}
	    	return amount;
	    }

	    private boolean canCondense() {
	        if (((ItemStack)this.condenserItemStacks.get(0)).isEmpty()) {
	            return false;
	        } else {
	        	ItemStack[] stacks = new ItemStack[] {this.condenserItemStacks.get(0),this.condenserItemStacks.get(1),this.condenserItemStacks.get(2),this.condenserItemStacks.get(3)};
	            ItemStack result = CondenserRecipes.instance().getCondensingResult(stacks);

	            if (result.isEmpty()) {
	                return false;
	            } else {
	                ItemStack output = this.condenserItemStacks.get(4);
	                
	                if (!output.isEmpty() && output.getItem() != result.getItem()) {
	                	return false;
	            	} else if (output.isEmpty() || output.isItemEqual(result)) {
	                    return true;
	                } else if (output.getCount() + result.getCount() <= this.getInventoryStackLimit() && output.getCount() + result.getCount() <= output.getMaxStackSize()) {       
	                    return true;
	                } else {
	                    return output.getCount() + result.getCount() <= result.getMaxStackSize();
	                }
	            }
	        }
	    }

	    public void condenseItem() {
	        if (this.canCondense()) {
	            ItemStack input1 = this.condenserItemStacks.get(0);
	            ItemStack input2 = this.condenserItemStacks.get(1);
	            ItemStack input3 = this.condenserItemStacks.get(2);
	            ItemStack input4 = this.condenserItemStacks.get(3);
	            ItemStack output = this.condenserItemStacks.get(4);
	            ItemStack[] inputs = new ItemStack[] {input1,input2,input3,input4};
	        	ItemStack result1 = CondenserRecipes.instance().getCondensingResult(inputs);
	            
	            ItemStack result = result1.copy();
	            
	            Random rand = new Random();
	            
	            int resultamount = rand.nextInt(upgradeOutput(result.getCount()));
	            
	            if(resultamount == 0) {
	            	resultamount = 1;
	            }
	            
	            result.setCount(resultamount);
	            
	            if (output.getCount() + result.getCount() <= this.getInventoryStackLimit() && 
	            		output.getCount() + result.getCount() <= output.getMaxStackSize()) {
	            	
	            	if (output.isEmpty()) {
		                this.condenserItemStacks.set(1, result.copy());
		            } else if (output.getItem() == result.getItem()) {
		            	output.grow(result.getCount());
		            }

		            input1.shrink(1);
		            input2.shrink(1);
		            input3.shrink(1);
		            input4.shrink(1);
	            }
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
