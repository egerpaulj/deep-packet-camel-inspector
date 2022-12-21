package pcap.master.pkts;

import java.io.IOException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.pkts.buffer.Buffer;
import io.pkts.packet.IPv4Packet;
import io.pkts.packet.IPv6Packet;
import io.pkts.packet.Packet;
import io.pkts.packet.TCPPacket;
import io.pkts.packet.UDPPacket;
import io.pkts.packet.impl.MACPacketImpl;
import io.pkts.packet.impl.SDPPacketImpl;
import io.pkts.packet.sip.SipParseException;
import io.pkts.packet.sip.impl.SipPacketImpl;
import io.pkts.protocol.Protocol;
import pcap.master.core.SocketDetails;

public class PktsPacketFactory implements IPktsPacketFactory {

	private int packetSequence = 0;

	private final Logger logger;

	public PktsPacketFactory() {
		logger = LoggerFactory.getLogger("PacketFactory");
	}
	
	@Override
	/**
	 * The sequence id of packets are maintained for logging. This should be reset when processing
	 * a second file. Pkts should provide the packet number
	 */
	public void resetSequenceCounter() {
		packetSequence = 0;
	}

	/*
	 * 
	 * @see pcap.master.camel.IPacketFactory#createPacket(io.pkts.packet.Packet)
	 */
	@Override
	public pcap.master.core.Packet createPacket(Packet packet) throws IOException {
		try {
			packetSequence++;
			// ToDo Until logging confirmed
			System.out.println("Sequence: " + packetSequence);
			logger.debug("Sequence: " + packetSequence);

			return getPacketData(packet);
		} catch (SipParseException ex) {
			logger.info("Sip Issue atfile entry sequence:" + packetSequence);
			logParsingIssueWithPacket(packet);
			pcap.master.core.Packet p = new pcap.master.core.Packet();
			p.setProtocol("sip");

			return p;
		}
	}

	private static pcap.master.core.Packet getPacketData(Packet packet) throws IOException {
		String sourceIpAddress = "";
		String destinationIpAddress = "";
		int destinationPort = 0;
		int sourcePort = 0;

		String protocolName = packet.getName();

		// Convert arrival time to java date
		Date packetArrivalDate = new Date(packet.getArrivalTime() * 1000L);

		// Get the packet information consider IP version 4 and 6
		if (packet.hasProtocol(Protocol.IPv6)) {
			IPv6Packet ip6Packet = (IPv6Packet) packet.getPacket(Protocol.IPv6);
			sourceIpAddress = ip6Packet.getSourceIP();
			destinationIpAddress = ip6Packet.getDestinationIP();

		} else if (packet.hasProtocol(Protocol.IPv4)) {
			IPv4Packet ip4Packet = (IPv4Packet) packet.getPacket(Protocol.IPv4);
			sourceIpAddress = ip4Packet.getSourceIP();
			destinationIpAddress = ip4Packet.getDestinationIP();
		} else {

			// DNS and other packets are classified as ETHERNET_II packets
			// logPacketAsUnknownVersion(packet);
		}

		// SDP
		if (packet.hasProtocol(Protocol.SDP)) {
			SDPPacketImpl sdpPacket = (SDPPacketImpl) packet.getPacket(Protocol.SDP);
			sourceIpAddress = sdpPacket.getName();
		}
		// SIP
		else if (packet.hasProtocol(Protocol.SIP)) {
			SipPacketImpl sipPacket = (SipPacketImpl) packet.getPacket(Protocol.SIP);
			sourceIpAddress = sipPacket.getRouteHeader().getAddress().getURI().toString();
		}

		// TCP
		else if (packet.hasProtocol(Protocol.TCP)) {

			TCPPacket tcpPacket = (TCPPacket) packet.getPacket(Protocol.TCP);

			destinationPort = tcpPacket.getDestinationPort();
			sourcePort = tcpPacket.getSourcePort();

			protocolName = "TCP";

		}
		// UDP
		else if (packet.hasProtocol(Protocol.UDP)) {
			UDPPacket udpPacket = (UDPPacket) packet.getPacket(Protocol.UDP);

			destinationPort = udpPacket.getDestinationPort();
			sourcePort = udpPacket.getSourcePort();

			protocolName = "UDP";
		} else if (packet.hasProtocol(Protocol.ARP)) {

			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.ARP);
		} else if (packet.hasProtocol(Protocol.ETHERNET_II)) {
			// LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: "+
			// Protocol.ETHERNET_II);

			MACPacketImpl p = (MACPacketImpl) packet.getPacket(Protocol.ETHERNET_II);
			destinationIpAddress = p.getDestinationMacAddress();
			sourceIpAddress = p.getSourceMacAddress();

		} else if (packet.hasProtocol(Protocol.ICMP)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.ICMP);
		} else if (packet.hasProtocol(Protocol.ICMP6)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.ICMP6);
		} else if (packet.hasProtocol(Protocol.IGMP)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.IGMP);
		} else if (packet.hasProtocol(Protocol.RTCP)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.RTCP);
		} else if (packet.hasProtocol(Protocol.RTP)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.RTP);
		} else if (packet.hasProtocol(Protocol.SCTP)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.SCTP);
		} else if (packet.hasProtocol(Protocol.SDP)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.SDP);
		} else if (packet.hasProtocol(Protocol.SIP)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.SIP);
		} else if (packet.hasProtocol(Protocol.SLL)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.SLL);
		} else if (packet.hasProtocol(Protocol.TLS)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.TLS);
		} else if (packet.hasProtocol(Protocol.UNKNOWN)) {
			LoggerFactory.getLogger("PacketFactory").warn("Unprocessed packet found: " + Protocol.UNKNOWN);
		}

		// Create the data needed for the pcap analyser.
		SocketDetails sourceSocket = new SocketDetails();
		sourceSocket.setIpAddress(sourceIpAddress);
		sourceSocket.setPort(sourcePort);

		SocketDetails destinationSocket = new SocketDetails();
		destinationSocket.setIpAddress(destinationIpAddress);
		destinationSocket.setPort(destinationPort);

		pcap.master.core.Packet parsedPacket = new pcap.master.core.Packet();
		parsedPacket.setConnectionDate(packetArrivalDate);
		parsedPacket.setProtocol(protocolName);
		parsedPacket.setSource(sourceSocket);
		parsedPacket.setDestination(destinationSocket);
		return parsedPacket;
	}

//	private void logPacketAsUnknownVersion(Packet packet) throws IOException {
//		String payload = getPayload(packet);
//		logger.warn("Unknown IP Version (not IPv4 and not IPv6 Packet - DNS?): " + payload);
//	}
	
	private void logParsingIssueWithPacket(Packet packet) throws IOException {
		String payload = getPayload(packet);
		logger.warn("issue parsing packet. Sequence: " + packetSequence + " payload: " + payload);
	}

	private String getPayload(Packet packet) throws IOException {
		Buffer buffer = packet.getPayload();
		String payload = "";
		while (buffer.hasReadableBytes()) {
			Buffer readBytes = buffer.readBytes(buffer.getReadableBytes());
			payload = readBytes.toString();
		}
		return payload;
	}
}
