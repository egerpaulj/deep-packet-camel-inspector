package pcap.master.camel.cameraServeillance;

import org.apache.camel.impl.SimpleRegistry;
import org.junit.Test;

import pcap.master.camel.PcapRouteBuilder;
import pcap.master.camel.test.PcapCamelRouteTest;

public class CameraServerillanceTest {

	@Test
	public void StartServeillance() throws Exception
	{
		PcapCamelRouteTest.runCamelTest(new CameraRouteBuilder());
	}
}
