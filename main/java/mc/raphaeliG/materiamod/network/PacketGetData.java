package mc.raphaeliG.materiamod.network;

import io.netty.buffer.ByteBuf;
import mc.raphaeliG.materiamod.tileentity.TileEntityCompressor;
import mc.raphaeliG.materiamod.util.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketGetData implements IMessage {

	
	private boolean messageValid;
	
	private BlockPos pos;

	private String className;
	private String fuelTimeField;
	private String currentItemFuelTimeField;
	private String compressTimeField;
	private String totalCompressTimeField;
	
	public PacketGetData() {
		this.messageValid = false;
	}
	
	public PacketGetData(BlockPos pos, String className, String fuelTimeField, String currentItemFuelTimeField, String compressTimeField,
			String totalCompressTimeField) {
		this.pos = pos;
		this.className = className;
		this.totalCompressTimeField = totalCompressTimeField;
		this.compressTimeField = compressTimeField;
		this.currentItemFuelTimeField = currentItemFuelTimeField;
		this.fuelTimeField = fuelTimeField;
		this.messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
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
	
	@Override
	public void toBytes(ByteBuf buf) {
		if (!this.messageValid)
			return;
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		ByteBufUtils.writeUTF8String(buf, this.className);
		ByteBufUtils.writeUTF8String(buf, this.fuelTimeField);
		ByteBufUtils.writeUTF8String(buf, this.currentItemFuelTimeField);
		ByteBufUtils.writeUTF8String(buf, this.compressTimeField);
		ByteBufUtils.writeUTF8String(buf, this.totalCompressTimeField);
	}
	
	public static class Handler implements IMessageHandler<PacketGetData, IMessage> {
		
		@Override
		public IMessage onMessage(PacketGetData message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER)
				return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(PacketGetData message, MessageContext ctx) {
			TileEntityCompressor te = (TileEntityCompressor) ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (te == null)
				return;
			PacketHandler.INSTANCE.sendTo(new PacketReturnData(te.fuelTime, te.currentItemFuelTime, te.compressTime, te.totalCompressTime,
					message.className, message.fuelTimeField, message.currentItemFuelTimeField, message.compressTimeField, message.totalCompressTimeField),
						ctx.getServerHandler().player);
			
		}
	}

}
