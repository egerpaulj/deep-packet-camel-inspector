package pcap.camel;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import org.pcap4j.core.PcapDumper;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PcapFileProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(PcapFileProducer.class);
    private PcapEndpoint endpoint;
    private PcapDumper pcapDumper;
    private PcapHandle pcapHandle;

    public PcapFileProducer(PcapEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        LOG.info("Sending PcapPacket");
        PcapPacket packet = (PcapPacket) exchange.getIn().getBody();

        pcapHandle = pcapHandle == null ? PcapHandleFactory.createHandle(endpoint) : pcapHandle;
        pcapDumper = pcapDumper == null ? pcapHandle.dumpOpen(endpoint.getName()) : pcapDumper;
        
        pcapDumper.dump(packet);
        pcapDumper.flush();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (pcapDumper != null)
            pcapDumper.close();

        if (pcapHandle != null)
            pcapHandle.close();
    }

}
