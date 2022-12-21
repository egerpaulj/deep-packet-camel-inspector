package pcap.master.decorator;

import java.util.HashMap;

import pcap.master.core.ISocketDecorator;
import pcap.master.core.ISocketDecoratorStrategy;
import pcap.master.core.SocketDetails;

// ToDo Evaluate memory usage on large processing. alternative massive amounts of duplicate processing.
public class CachedDecorator implements ISocketDecorator {
	private final HashMap<String, SocketDetails> hostnameDecorationMap;
	private final HashMap<String, SocketDetails> geoInfoDecorationMap;
	private final HashMap<String, SocketDetails> tracerouteDecorationMap;

	private final ISocketDecoratorStrategy hostnameDecorator;
	private final ISocketDecoratorStrategy geoInfoDecorator;
	private final ISocketDecoratorStrategy traceRouteDecorator;

	public CachedDecorator(
			ISocketDecoratorStrategy hostnameDecorator, 
			ISocketDecoratorStrategy geoInfoDecorator,
			ISocketDecoratorStrategy traceRouteDecorator) {
		super();
		this.hostnameDecorator = hostnameDecorator;
		this.geoInfoDecorator = geoInfoDecorator;
		this.traceRouteDecorator = traceRouteDecorator;

		this.hostnameDecorationMap = new HashMap<String, SocketDetails>();
		this.geoInfoDecorationMap = new HashMap<String, SocketDetails>();
		this.tracerouteDecorationMap = new HashMap<String, SocketDetails>();
	}

	/* (non-Javadoc)
	 * @see pcap.master.decorator.ISocketDecorator#decorateHostname(pcap.master.core.SocketDetails)
	 */
	@Override
	public void decorateHostname(SocketDetails socketDetails) throws Exception {
		String ipAddress = socketDetails.getIpAddress();
		// Synchronise to deal with parallel thread decoration
		synchronized (hostnameDecorationMap) {
			if (hostnameDecorationMap.containsKey(ipAddress)) {
				// Already decorated => no need to perform a reverse lookup
				return;
			}

			hostnameDecorator.decorate(socketDetails);

			hostnameDecorationMap.put(socketDetails.getIpAddress(), socketDetails);
		}
	}
	
	/* (non-Javadoc)
	 * @see pcap.master.decorator.ISocketDecorator#decorateGeoInfo(pcap.master.core.SocketDetails)
	 */
	@Override
	public void decorateGeoInfo(SocketDetails socketDetails) throws Exception {
		String ipAddress = socketDetails.getIpAddress();
		// Synchronise to deal with parallel thread decoration
		synchronized (geoInfoDecorationMap) {
			if (geoInfoDecorationMap.containsKey(ipAddress)) {
				// Already decorated => no need to perform a reverse lookup
				return;
			}

			geoInfoDecorator.decorate(socketDetails);

			geoInfoDecorationMap.put(socketDetails.getIpAddress(), socketDetails);
		}
	}
	
	/* (non-Javadoc)
	 * @see pcap.master.decorator.ISocketDecorator#decorateTraceroute(pcap.master.core.SocketDetails)
	 */
	@Override
	public void decorateTraceroute(SocketDetails socketDetails) throws Exception {
		String ipAddress = socketDetails.getIpAddress();
		// Synchronise to deal with parallel thread decoration
		synchronized (tracerouteDecorationMap) {
			if (tracerouteDecorationMap.containsKey(ipAddress)) {
				// Already decorated => no need to perform a reverse lookup
				return;
			}

			traceRouteDecorator.decorate(socketDetails);

			tracerouteDecorationMap.put(socketDetails.getIpAddress(), socketDetails);
		}
	}

}
