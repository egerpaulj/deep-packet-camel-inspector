package pcap.master.core;

import java.io.Serializable;
import java.util.Date;

public class Packet implements Serializable{
	private static final long serialVersionUID = 1L;
	private Date connectionDate;
	private String protocol = "undefined";

	private SocketDetails source = new SocketDetails();;
	private SocketDetails destination = new SocketDetails();

	public Date getConnectionDate() {
		return connectionDate;
	}

	public void setConnectionDate(Date connectionDate) {
		this.connectionDate = connectionDate;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public SocketDetails getSource() {
		return source;
	}

	public void setSource(SocketDetails source) {
		this.source = source;
	}

	public SocketDetails getDestination() {
		return destination;
	}

	public void setDestination(SocketDetails destination) {
		this.destination = destination;
	}
	
	@Override
	public String toString() {
		return connectionDate +" " + protocol+ " Src: " + source.toString() + " Dst: " + destination.toString();
	}

}
