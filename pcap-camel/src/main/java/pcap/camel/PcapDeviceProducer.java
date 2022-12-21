package pcap.camel;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PcapDeviceProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(PcapDeviceProducer.class);
    private PcapEndpoint endpoint;
    private PcapHandle pcapHandle;

    public PcapDeviceProducer(PcapEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.info("Sending PcapPacket");
        PcapPacket packet = (PcapPacket) exchange.getIn().getBody();

        pcapHandle = pcapHandle == null ? PcapHandleFactory.createHandle(endpoint) : pcapHandle;

        pcapHandle.sendPacket(packet);
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();

        pcapHandle.close();
    }
}
