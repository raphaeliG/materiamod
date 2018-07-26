package mc.raphaeliG.materiamod.init;

import java.util.ArrayList;
import java.util.List;

import mc.raphaeliG.materiamod.objects.blocks.BlockCompressor;
import mc.raphaeliG.materiamod.objects.blocks.templates.BlockCompressedMaterial;
import mc.raphaeliG.materiamod.objects.blocks.templates.BlockMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockInit {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block COPPER_BLOCK = new BlockMaterial("copper_block", MaterialInit.BLOCK_COPPER, 1, SoundType.METAL);
	
	public static final Block COMPRESSED_IRON_BLOCK = new BlockCompressedMaterial("compressed_iron_block", Material.IRON, 1, SoundType.METAL);
	
	public static final Block SINTERING_FURNACE = new BlockCompressor();
}
