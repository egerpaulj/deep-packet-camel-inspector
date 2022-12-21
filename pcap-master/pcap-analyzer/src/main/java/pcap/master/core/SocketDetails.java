package pcap.master.core;

import java.io.Serializable;

import pcap.master.core.GeoData;

public class SocketDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ipAddress="undefined";
	private int port =0;
	private String hostName ="undefined";
	private GeoData geoData;
	
	// ToDo More information than just the string output? By including hops explicitly route changes can be monitored.
	private String traceRoute="undefined";

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public GeoData getGeoData() {
		return geoData;
	}

	public void setGeoData(GeoData geoData) {
		this.geoData = geoData;
	}

	public String getTraceRoute() {
		return traceRoute;
	}

	public void setTraceRoute(String traceRoute) {
		this.traceRoute = traceRoute;
	}
	
	@Override
	public String toString() {
		return ipAddress +":"+ port; 
	}

}
