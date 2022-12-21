package pcap.master.core;

import pcap.master.core.SocketDetails;

public interface ISocketDecoratorStrategy {
	/**
	 * Decorates the socket details with additional information from other sources.
	 * @param socketDetails
	 */
	void decorate(SocketDetails socketDetails) throws Exception;
}
