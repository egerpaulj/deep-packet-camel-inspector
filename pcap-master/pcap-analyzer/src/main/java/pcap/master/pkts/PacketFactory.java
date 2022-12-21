package pcap.master.pkts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import pcap.master.core.IPacketFactory;
import pcap.master.core.Packet;

public class PacketFactory implements IPacketFactory {

	final IPktsPacketFactory packetFactory;
	
	// ToDo Temp code
	private static int count=0;

	public PacketFactory(IPktsPacketFactory packetFactory) {
		super();
		this.packetFactory = packetFactory;
	}

	/* (non-Javadoc)
	 * @see pcap.master.camel.IPacketFactory#createPackets(java.util.List, java.lang.String)
	 */
	@Override
	public void createPackets(List<Packet> packets, String path) throws FileNotFoundException, IOException {
		// using pkts Pcap to parse the packet data.
		Pcap pcapStream = Pcap.openStream(path);
		
		// Loop through all the packets in the pcap file and add the packet information
		// to the list.
		pcapStream.loop(new PacketHandler() {

			@Override
			public boolean nextPacket(io.pkts.packet.Packet packet) throws IOException {
				count++;
				
				if(count > 100)
					return true;
				
				// Create and a packet to the list.
				packets.add(packetFactory.createPacket(packet));
				return true;
			}

		});
		
		packetFactory.resetSequenceCounter();
	}

}