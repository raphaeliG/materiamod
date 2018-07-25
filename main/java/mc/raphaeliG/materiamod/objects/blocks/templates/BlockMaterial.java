package mc.raphaeliG.materiamod.objects.blocks.templates;

import mc.raphaeliG.materiamod.init.CreativeTabInit;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMaterial extends BlockBase {

	public BlockMaterial(String name, Material materialIn, int toolLevel, SoundType sound) {
		super(name, materialIn, CreativeTabInit.MAIN, 5.0f, "pickaxe", toolLevel, 30.0f, sound);
		// TODO Auto-generated constructor stub
	}

}
