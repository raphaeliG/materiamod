package mc.raphaeliG.materiamod.objects.items;

import mc.raphaeliG.materiamod.Materiamod;
import mc.raphaeliG.materiamod.init.ItemInit;
import mc.raphaeliG.materiamod.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

public class ToolAxe extends ItemAxe implements IHasModel{

	public ToolAxe(String name, ToolMaterial material, float damage, float speed, CreativeTabs tab, int harvestLevel, int durability) {
		super(material, damage, speed);
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(tab);
		setHarvestLevel("axe", harvestLevel);
		setMaxDamage(durability);
		attackDamage = damage;
		attackSpeed = speed;
		
		ItemInit.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		// TODO Auto-generated method stub
		Materiamod.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
