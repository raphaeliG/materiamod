package mc.raphaeliG.materiamod.client.gui;

import mc.raphaeliG.materiamod.container.compressor.ContainerCompressor;
import mc.raphaeliG.materiamod.tileentity.TileEntityCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int COMPRESSOR = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case COMPRESSOR:
			return new ContainerCompressor(player.inventory, (TileEntityCompressor) world.getTileEntity(new BlockPos(x, y, z)));
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case COMPRESSOR:
			return new GuiCompressor(player.inventory, (TileEntityCompressor) world.getTileEntity(new BlockPos(x, y, z)));
		default:
			return null;
		}
	}

}
