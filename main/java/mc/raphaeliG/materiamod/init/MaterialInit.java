package mc.raphaeliG.materiamod.init;

import mc.raphaeliG.materiamod.objects.materials.MaterialCopper;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class MaterialInit {
	//Block Material
	public static final Material BLOCK_COPPER = new MaterialCopper();
	//Tool Material
	public static final ToolMaterial TOOL_COPPER = EnumHelper.addToolMaterial("copper", 2, 251, 4.0f, 9, 14);
}
