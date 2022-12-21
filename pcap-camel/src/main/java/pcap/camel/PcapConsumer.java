package pcap.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultConsumer;

import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pcap4j.core.PcapPacket;

import java.util.concurrent.ExecutorService;

public class PcapConsumer extends DefaultConsumer {
    private final PcapEndpoint endpoint;

    private ExecutorService executorService;
    private PcapHandle pcapHandle;

    private static final Logger LOG = LoggerFactory.getLogger(PcapConsumer.class);

    public PcapConsumer(PcapEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        pcapHandle = PcapHandleFactory.createHandle(endpoint);

        // start a single threaded pool to monitor events
        executorService = endpoint.createExecutor();
        Exchange exchange = endpoint.createExchange();

        // submit task to the thread pool
        executorService.submit(() -> {
            // subscribe to an event
            try {
                pcapHandle.loop(-1, new PacketListener() {

                    @Override
                    public void gotPacket(PcapPacket arg0) {
                        LOG.info("Received Packet: " + arg0.getClass().getSimpleName());

                        exchange.getIn().setBody(arg0);
                        try {
                            getProcessor().process(exchange);
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                            LOG.error(e.getStackTrace().toString());
                        }
                    }
                });
            } catch (Exception e) {
                LOG.error(e.getMessage());
                LOG.error(e.getStackTrace().toString());
            }
        });
    }

    @Override
    protected void doStop() throws Exception {
        pcapHandle.breakLoop();
        pcapHandle.close();

        super.doStop();

        // shutdown the thread pool gracefully
        getEndpoint().getCamelContext().getExecutorServiceManager().shutdownGraceful(executorService);
    }
}
