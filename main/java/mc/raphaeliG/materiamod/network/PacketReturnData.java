package mc.raphaeliG.materiamod.network;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;
import mc.raphaeliG.materiamod.recipes.compressor.CompressorRecipeHelper;
import mc.raphaeliG.materiamod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketReturnData implements IMessage {
	
	private boolean messageValid;

	private int fuelTime;
	private int currentItemFuelTime;
	private int compressTime;
	private int totalCompressTime;

	private String className;
	private String fuelTimeField;
	private String currentItemFuelTimeField;
	private String compressTimeField;
	private String totalCompressTimeField;
	
	public PacketReturnData() {
		this.messageValid = false;
	}
	
	public PacketReturnData(int fuelTime, int currentItemFuelTime, int compressTime, int totalCompressTime, String className, String fuelTimeField,
			String currentItemFuelTimeField, String compressTimeField, String totalCompressTimeField) {
		this.className = className;
		this.fuelTime = fuelTime;
		this.currentItemFuelTime = currentItemFuelTime;
		this.compressTime = compressTime;
		this.totalCompressTime = totalCompressTime;
		this.fuelTimeField = fuelTimeField;
		this.currentItemFuelTimeField = currentItemFuelTimeField;
		this.compressTimeField = compressTimeField;
		this.totalCompressTimeField = totalCompressTimeField;
		this.messageValid = true;
	}

	/**
	 * Reads all of the data from the provided {@link ByteBuf}
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.fuelTime = buf.readInt();
			this.currentItemFuelTime = buf.readInt();
			this.compressTime = buf.readInt();
			this.totalCompressTime = buf.readInt();
			this.className = ByteBufUtils.readUTF8String(buf);
			this.fuelTimeField = ByteBufUtils.readUTF8String(buf);
			this.currentItemFuelTimeField = ByteBufUtils.readUTF8String(buf);
			this.compressTimeField = ByteBufUtils.readUTF8String(buf);
			this.totalCompressTimeField = ByteBufUtils.readUTF8String(buf);
		} catch (IndexOutOfBoundsException ioe) {
			Reference.logger.catching(ioe);
			return;
		}
		this.messageValid = true;
	}

	/**
	 * Writes all of the data to the provided {@link ByteBuf}
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		if (!this.messageValid)
			return;
		buf.writeInt(this.fuelTime);
		buf.writeInt(this.currentItemFuelTime);
		buf.writeInt(this.compressTime);
		buf.writeInt(this.totalCompressTime);
		ByteBufUtils.writeUTF8String(buf, this.className);
		ByteBufUtils.writeUTF8String(buf, this.fuelTimeField);
		ByteBufUtils.writeUTF8String(buf, this.currentItemFuelTimeField);
		ByteBufUtils.writeUTF8String(buf, this.compressTimeField);
		ByteBufUtils.writeUTF8String(buf, this.totalCompressTimeField);
	}
	
	public static class Handler implements IMessageHandler<PacketReturnData, IMessage> {
		
		private World world;
		
		@Override
		public IMessage onMessage(PacketReturnData message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT)
				return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		void processMessage(PacketReturnData message) {
			Reference.logger.info("processing a packet return data!");
			try {
				//if (world.isBlockLoaded(message.))
				Class clazz = Class.forName(message.className);
				Field fuelTimeField = clazz.getDeclaredField(message.fuelTimeField);
				Field currentItemFuelTimeField = clazz.getDeclaredField(message.currentItemFuelTimeField);
				Field compressTimeField = clazz.getDeclaredField(message.compressTimeField);
				Field totalCompressTimeField = clazz.getDeclaredField(message.totalCompressTimeField);
				fuelTimeField.setInt(clazz, message.fuelTime);
				currentItemFuelTimeField.setInt(clazz, message.currentItemFuelTime);
				compressTimeField.setInt(clazz, message.compressTime);
				totalCompressTimeField.setInt(clazz, message.totalCompressTime);
			} catch (Exception e) {
				Reference.logger.catching(e);
			}
		}

	}

}
