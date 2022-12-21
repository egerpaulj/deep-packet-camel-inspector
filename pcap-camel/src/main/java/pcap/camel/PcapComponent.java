package pcap.camel;

import java.util.Map;

import org.apache.camel.Endpoint;

import org.apache.camel.support.DefaultComponent;

@org.apache.camel.spi.annotations.Component("pcap")
public class PcapComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        PcapEndpoint endpoint = new PcapEndpoint(uri, this);
        setProperties(endpoint, parameters);
        PcapType type = PcapType.valueOf(remaining);
        endpoint.setPcapType(type);
        return endpoint;
    }
}
