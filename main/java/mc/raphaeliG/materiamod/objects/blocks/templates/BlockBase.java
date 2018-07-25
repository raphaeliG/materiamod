package mc.raphaeliG.materiamod.objects.blocks.templates;

import mc.raphaeliG.materiamod.Materiamod;
import mc.raphaeliG.materiamod.init.BlockInit;
import mc.raphaeliG.materiamod.init.ItemInit;
import mc.raphaeliG.materiamod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel{

	public BlockBase(String name, Material materialIn, CreativeTabs cretiveTab, float hardness, String toolClass, int toolLevel, float resistance, SoundType sound) {
		super(materialIn);
		
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(cretiveTab);
		setHardness(hardness);
		setHarvestLevel(toolClass, toolLevel);
		setResistance(resistance);
		setSoundType(sound);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(getRegistryName()).setUnlocalizedName(getUnlocalizedName()));
	}

	@Override
	public void registerModels() {
		Materiamod.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
