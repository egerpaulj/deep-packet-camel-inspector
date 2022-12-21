package pcap.master.decorator;

import pcap.master.core.ISocketDecoratorStrategy;
import pcap.master.core.SocketDetails;

public class GeoInfoIpLocateDecoratorStrategy implements ISocketDecoratorStrategy {

	@Override
	public void decorate(SocketDetails socketDetails) throws Exception {
		throw new Exception("Not yet implemented => Implement with HTTP client");
	}

}
