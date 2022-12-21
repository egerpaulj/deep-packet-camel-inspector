package pcap.master.decorator;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;

import pcap.master.core.ISocketDecoratorStrategy;
import pcap.master.core.SocketDetails;

public class HostnameDecoratorStrategy implements ISocketDecoratorStrategy{

	private Logger logger;
	@Override
	public void decorate(SocketDetails socketDetails) {
		try {
			InetAddress address = InetAddress.getByName(socketDetails.getIpAddress());
			socketDetails.setHostName(address.getHostName());
		} catch (UnknownHostException e) {
			// all IPs don't have a host name
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
			
			socketDetails.setHostName("unknown-" + socketDetails.getIpAddress());
		}
	}

}
