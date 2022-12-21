package main.java.pcap.master.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.pcap.master.core.ISocketDecorator;
import main.java.pcap.master.core.Packet;
import main.java.pcap.master.core.SocketDetails;

public class PacketDecoratorAllBean {
	private final ISocketDecorator socketDecorator;
	private final Logger logger;
	/**
	 * If true exceptions are only logged. Otherwise the bean throws an error. The message should be routed to an error path.
	 */
	private boolean allowFaults = true;

	public PacketDecoratorAllBean(ISocketDecorator socketDecorator) {
		super();
		this.socketDecorator = socketDecorator;
		this.logger = LoggerFactory.getLogger(PacketDecoratorAllBean.class);
	}
	
	public PacketDecoratorAllBean(ISocketDecorator socketDecorator, boolean allowFaults) {
		this(socketDecorator);
		this.allowFaults = allowFaults;
		
	}

	public void decorate(Packet packet) throws Exception {
		SocketDetails source = packet.getSource();
		SocketDetails destination= packet.getDestination();

		try {
			socketDecorator.decorateHostname(source);
		} catch (Exception e) {
			logException(e, "Failed to decorate source hostname", source);
		}

		try {
			socketDecorator.decorateHostname(destination);
		} catch (Exception e) {
			logException(e, "Failed to decorate destination hostname", destination);
		}

		try {
			socketDecorator.decorateGeoInfo(source);
		} catch (Exception e) {
			logException(e, "Failed to decorate source geo information", source);
		}

		try {
			socketDecorator.decorateGeoInfo(destination);
		} catch (Exception e) {
			logException(e, "Failed to decorate destination geo information", destination);
		}

		try {
			socketDecorator.decorateTraceroute(source);
		} catch (Exception e) {
			logException(e, "Failed to decorate source traceroute information", source);
		}
		try {
			socketDecorator.decorateTraceroute(destination);
		} catch (Exception e) {
			logException(e, "Failed to decorate destination tracerouteinformation", destination);
		}
	}

	private void logException(Exception e, String message, SocketDetails socketDetails) throws Exception {
		logger.error(message + " " + socketDetails.getIpAddress());
		logger.error(e.getMessage());
		logger.error(e.getStackTrace().toString());
		
		if(!allowFaults) {
			throw new Exception("Error processing socket: " + socketDetails.toString());
		}
	}
}
