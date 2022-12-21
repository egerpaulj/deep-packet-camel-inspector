package pcap.master.core;

import pcap.master.core.SocketDetails;

public interface ISocketDecorator {

	/**
	 * Hostname is resolved for the IP Address.
	 * @param socketDetails
	 * @throws Exception 
	 */
	void decorateHostname(SocketDetails socketDetails) throws Exception;

	/**
	 * Geo information is decorated for the socketDetails.
	 * @param socketDetails
	 */
	void decorateGeoInfo(SocketDetails socketDetails) throws Exception;

	/**
	 * Traceroute information is decorated for the socketDetails.
	 * @param socketDetails
	 */
	void decorateTraceroute(SocketDetails socketDetails) throws Exception;

}