package pcap.master.pkts;

import java.io.IOException;

import io.pkts.packet.Packet;

public interface IPktsPacketFactory {

	pcap.master.core.Packet createPacket(Packet packet) throws IOException;

	/**
	 * The sequence id of packets are maintained for logging. This should be reset when processing
	 * a second file. Pkts should provide the packet number
	 */
	void resetSequenceCounter();

}