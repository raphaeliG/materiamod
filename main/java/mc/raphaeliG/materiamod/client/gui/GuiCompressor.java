package mc.raphaeliG.materiamod.client.gui;

import mc.raphaeliG.materiamod.container.compressor.ContainerCompressor;
import mc.raphaeliG.materiamod.objects.blocks.compressor.TileEntityCompressor;
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
	
	public GuiCompressor(IInventory playerInventory, TileEntityCompressor tileEntity) {
		super(new ContainerCompressor(playerInventory, tileEntity));
		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;
		
		xSize = 176;
		ySize = 166;
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/compressor.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		//String s = I18n.format("container.compressor"); //Gets the formatted name for the block breaker from the language file - NOTE ADD "container.block_breaker=Block Breaker" to the language file (without quotes) and then delete this note
		//mc.fontRenderer.drawString(s, guiLeft + xSize / 2 - mc.fontRenderer.getStringWidth(s) / 2, guiTop + 6, 4210752); //Draws the block breaker name in the center on the top of the gui
		//mc.fontRenderer.drawString(playerInventory.getDisplayName().getFormattedText(), guiLeft + 8, guiTop + 72, 4210752); //The player's inventory name
		}
	
	@Override

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format("container.compressor.name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 3, 0x404040);
        fontRenderer.drawString(playerInventory.getDisplayName().getFormattedText(), 8, 72, 4210752);
    }

}
