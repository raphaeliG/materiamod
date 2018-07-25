package mc.raphaeliG.materiamod.objects.blocks.templates;

import mc.raphaeliG.materiamod.Materiamod;
import mc.raphaeliG.materiamod.init.BlockInit;
import mc.raphaeliG.materiamod.init.ItemInit;
import mc.raphaeliG.materiamod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;

public class BlockNotFull extends BlockBase implements IHasModel{

	public BlockNotFull(String name, Material materialIn, CreativeTabs cretiveTab, float hardness, String toolClass, int toolLevel, float resistance, SoundType sound) {
		super(name, materialIn, cretiveTab, hardness, toolClass, toolLevel, resistance, sound);
	}

	@Override
	public void registerModels() {
		Materiamod.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		// TODO Auto-generated method stub
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
