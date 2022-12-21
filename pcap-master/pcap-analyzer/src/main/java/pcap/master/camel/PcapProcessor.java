package pcap.master.camel;

import pcap.master.core.IPacketFactory;
import pcap.master.core.Packet;
import pcap.master.pkts.IPktsPacketFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;

/**
 * Processes a PCAP file and converts them into a list of Packet information.
 * 
 * @author egerpaul
 *
 */
public class PcapProcessor implements Processor {

	final IPacketFactory packetFactory;

	public PcapProcessor(IPacketFactory packetFactory) {
		super();
		this.packetFactory = packetFactory;
	}

	@Override
	public void process(Exchange exchange) throws Exception {

		List<Packet> packets = new ArrayList<Packet>();

		GenericFile<?> file = (GenericFile<?>) exchange.getIn().getBody();

		packetFactory.createPackets(packets, file.getAbsoluteFilePath());

		exchange.getOut().setBody(packets);
	}

}
