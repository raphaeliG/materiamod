package mc.raphaeliG.materiamod.objects.blocks;

import mc.raphaeliG.materiamod.Materiamod;
import mc.raphaeliG.materiamod.client.gui.GuiHandler;
import mc.raphaeliG.materiamod.init.CreativeTabInit;
import mc.raphaeliG.materiamod.objects.blocks.templates.BlockNotFull;
import mc.raphaeliG.materiamod.tileentity.TileEntityCompressor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BlockCompressor extends BlockNotFull implements ITileEntityProvider{

	public BlockCompressor() {
		super("compressor", Material.IRON, CreativeTabInit.MACHINES, 0, "pickaxe", 1, 0, SoundType.METAL);
		
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCompressor();
	}
	
	@Override
	public boolean hasTileEntity() {
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		IItemHandler handler = ((TileEntityCompressor)worldIn.getTileEntity(pos)).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < handler.getSlots(); ++i)
		{
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote)
		{
			playerIn.openGui(Materiamod.instance, GuiHandler.COMPRESSOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
