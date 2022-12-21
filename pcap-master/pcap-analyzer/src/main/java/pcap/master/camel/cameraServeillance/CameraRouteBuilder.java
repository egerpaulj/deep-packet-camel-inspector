package main.java.pcap.master.camel.cameraServeillance;

import org.apache.camel.builder.RouteBuilder;

public class CameraRouteBuilder extends RouteBuilder{

	private String path = "/home/egerpaul/motionCapture";
	
	
	public CameraRouteBuilder()
	{
		
	}
	
	public CameraRouteBuilder(String path) {
		super();
		if(path != null) {
			this.path = path;	
		}
	}



	@Override
	public void configure() throws Exception {
		// 
		from("webcam:serv?motion=true")
		.to("file:" + path);
		
	}

}
