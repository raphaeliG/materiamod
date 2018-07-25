package mc.raphaeliG.materiamod.objects.items;

import mc.raphaeliG.materiamod.Materiamod;
import mc.raphaeliG.materiamod.init.ItemInit;
import mc.raphaeliG.materiamod.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel{
	
	public ItemBase(String name, CreativeTabs cretiveTab) {
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(cretiveTab);
		
		ItemInit.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		Materiamod.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
