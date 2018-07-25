package mc.raphaeliG.materiamod.proxy;

import mc.raphaeliG.materiamod.Materiamod;
import mc.raphaeliG.materiamod.client.gui.GuiHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Materiamod.instance, new GuiHandler());
	}
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
}
