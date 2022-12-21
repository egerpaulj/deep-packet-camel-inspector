package pcap.camel;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import java.util.concurrent.ExecutorService;

/**
 * pcap component gets packet data from a Network Interface.
 *
 */
@UriEndpoint(firstVersion = "1.0-SNAPSHOT", scheme = "pcap", title = "pcap", syntax = "pcap:pcaptype", consumerClass = PcapConsumer.class, label = "Pcap")
public class PcapEndpoint extends DefaultEndpoint {

    @UriPath
    @Metadata(required = true)
    private PcapType pcapType;

    @UriParam
    @Metadata(required = true)
    private String name;

    @UriParam(defaultValue = "65536")
    private int snapLength = 65536;

    @UriParam(defaultValue = "10")
    private int timeout = 10;

    @UriParam(defaultValue = "true")
    private boolean promiscuousMode = true;

    @UriParam(defaultValue = "")
    private String filter = "";

    public PcapEndpoint() {
    }

    public PcapEndpoint(String uri, PcapComponent component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        if (getPcapType() == PcapType.device)
            return new PcapDeviceProducer(this);

        return new PcapFileProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        Consumer consumer = new PcapConsumer(this, processor);
        configureConsumer(consumer);
        return consumer;
    }

    /**
     * Network interface name or file name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExecutorService createExecutor() {
        return getCamelContext().getExecutorServiceManager().newSingleThreadExecutor(this, "pcapConsumer");
    }

    public int getSnapLength() {
        return snapLength;
    }

    /**
     * PcapLib Packet sniff SnapLength
     * 
     * @param snapLength
     */
    public void setSnapLength(int snapLength) {
        this.snapLength = snapLength;
    }

    public int getTimeout() {
        return timeout;
    }

    /**
     * Pcaplib timeout
     * 
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isPromiscuousMode() {
        return promiscuousMode;
    }

    /**
     * Pcap Lib Promiscuous Mode
     * 
     * @param promiscuousMode
     */
    public void setPromiscuousMode(boolean promiscuousMode) {
        this.promiscuousMode = promiscuousMode;
    }

    public PcapType getPcapType() {
        return pcapType;
    }

    /**
     * Set the Pcap type. Network Device or File Based
     * 
     * @param pcapType
     */
    public void setPcapType(PcapType pcapType) {
        this.pcapType = pcapType;
    }

    public String getFilter() {
        return filter;
    }

    /**
     * Sets the BPF Filter.
     * @param filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }
}
