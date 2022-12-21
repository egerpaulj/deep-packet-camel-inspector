package pcap.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class pcapComponentTest extends CamelTestSupport {


    @Test
    public void testpcap() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(5);

        mock.await();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("pcap:device?name=eth0&filter=udp+port+53")
                  .to("pcap:file?name=testOutput.pcap")
                  .to("mock:result");
            }
        };
    }
}
