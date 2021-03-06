package mc.raphaeliG.materiamod.client.gui;

import mc.raphaeliG.materiamod.container.compressor.ContainerCompressor;
import mc.raphaeliG.materiamod.network.PacketGetData;
import mc.raphaeliG.materiamod.network.PacketHandler;
import mc.raphaeliG.materiamod.tileentity.TileEntityCompressor;
import mc.raphaeliG.materiamod.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiCompressor extends GuiContainer {
	
	private IInventory playerInventory;
	private TileEntityCompressor tileEntity;
	
	public static int fuelTime = 0, currentItemFuelTime = 0, compressTime = 0, totalCompressTime = 0;
	public static int sync = 0;
	
	public GuiCompressor(IInventory playerInventory, TileEntityCompressor tileEntity) {
		super(new ContainerCompressor(playerInventory, tileEntity));
		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;
		
		xSize = 176;
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		//tileEntity = (TileEntityCompressor)tileEntity.getWorld().getTileEntity(tileEntity.getPos());
		drawDefaultBackground();
		mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/compressor.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		tileEntity.readFromNBT(tileEntity.getUpdatePacket().getNbtCompound());
		String name = I18n.format("container.compressor.name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 3, 0x404040);
        fontRenderer.drawString(playerInventory.getDisplayName().getFormattedText(), 8, 72, 4210752);
        
        sync = (sync + 1) % 20;
        if (sync == 0) PacketHandler.INSTANCE.sendToServer(new PacketGetData(tileEntity.getPos(),
        		"mc.raphaeliG.materiamod.client.gui.GuiCompressor", "fuelTime", "currentItemFuelTime", "compressTime", "totalCompressTime"));
        
        fontRenderer.drawString("CT" + tileEntity.compressTime, 4, 4, 0x404040);
    }

}
