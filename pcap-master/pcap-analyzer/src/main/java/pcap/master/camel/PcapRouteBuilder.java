package pcap.master.camel;

import org.apache.camel.builder.RouteBuilder;

import pcap.master.pkts.PacketFactory;
import pcap.master.pkts.PktsPacketFactory;

public class PcapRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// ToDo Camel routes from XML
		// Polling delay by 5000ms. Default is 500ms which is overloading disk access.
		from("file:/home/egerpaul/workspaces/networkAnalyser/pcapAnalyser/testFiles?delay=5000")
				// ToDo Dependency injection
				// The Processor creates a list of Packets from the PCAP file.
				.process(new PcapProcessor(new PacketFactory(new PktsPacketFactory())))
				// Body is a list of packets - hence will be split into a stream of packets
				.split(simple("${body}"))
				.log("Processed pcap file: ${body}")
				.to("seda:packets");
	
		// Decorate the packets using 10 parallel threads
		from("seda:packets?concurrentConsumers=1")
		.doTry()
		.bean("decoratorBean")
		.log("Decoration successful: ${body}")
		// .to ToDo Once decorated, send To ES
		.doCatch(Exception.class)
		// .to Errors ToDo To Error MQ
		.log("Error processing: ${body}");
	}

}
