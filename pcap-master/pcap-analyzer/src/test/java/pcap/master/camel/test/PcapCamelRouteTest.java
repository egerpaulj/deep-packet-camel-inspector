package pcap.master.camel.test;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.SimpleRegistry;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import pcap.master.camel.PacketDecoratorAllBean;
import pcap.master.camel.PcapRouteBuilder;
import pcap.master.core.ISocketDecorator;
import pcap.master.decorator.CachedDecorator;
import pcap.master.decorator.GeoInfoIpLocateDecoratorStrategy;
import pcap.master.decorator.HostnameDecoratorStrategy;
import pcap.master.decorator.TracerouteDecoratorStrategy;

public class PcapCamelRouteTest {

	@Test
	public void TestPcapFileProcessing() throws Exception {
		// ToDo Dependency injection (Spring)
		ISocketDecorator sockerDecorator = new CachedDecorator(
				new HostnameDecoratorStrategy(), 
				new GeoInfoIpLocateDecoratorStrategy(), 
				new TracerouteDecoratorStrategy());
		
		PacketDecoratorAllBean decoratorBean = new PacketDecoratorAllBean(sockerDecorator);
		
		
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("decoratorBean", decoratorBean);
		
		
		runCamelTest(new PcapRouteBuilder(), registry);
	}

	public static void runCamelTest(RouteBuilder routeBuilder, SimpleRegistry registry) throws Exception {
		
		CamelContext context = new DefaultCamelContext();
		if (registry != null) {
			context = new DefaultCamelContext(registry);
		}

		context.addRoutes(routeBuilder);

		context.start();
		System.out.println("Hit a key to end Test.");
		System.in.read();
		context.stop();
	}

	public static void runCamelTest(RouteBuilder routeBuilder) throws Exception {
		runCamelTest(routeBuilder, null);
	}

}
