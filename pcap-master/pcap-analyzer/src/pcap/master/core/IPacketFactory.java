package pcap.master.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IPacketFactory {

	/**
	 * Creates packets by parsing the file. Adds the parsed packets to the list.
	 * 
	 * @param packets add packets to the list.
	 * @param path the pcap file path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void createPackets(List<Packet> packets, String path) throws FileNotFoundException, IOException;

}