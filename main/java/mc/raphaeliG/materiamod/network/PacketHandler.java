package mc.raphaeliG.materiamod.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static SimpleNetworkWrapper INSTANCE;
	private static int ID = 0;
	private static int nextID() {
		return ID++;
	}
	
	public static void registerMessages(String channelName) {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);

		// Server packets
		INSTANCE.registerMessage(PacketGetData.Handler.class, PacketGetData.class, nextID(), Side.SERVER);

		// Client packets
		INSTANCE.registerMessage(PacketReturnData.Handler.class, PacketReturnData.class, nextID(), Side.CLIENT);
	}

}
