package main.java.pcap.master.camel.cameraServeillance;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class Starter {

	public static void main(String [] args) throws Exception
	{
		String targetPath = null;
		
		
		if(args.length > 0) {
			targetPath = args[0];
		}
		
		CameraRouteBuilder routeBuilder = new CameraRouteBuilder(targetPath);
		
		CamelContext context = new DefaultCamelContext();

		context.addRoutes(routeBuilder);

		context.start();
		System.out.println("Hit a key to end Test.");
		System.in.read();
		context.stop();
	}
}
