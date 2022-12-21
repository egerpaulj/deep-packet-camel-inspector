package pcap.camel;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.namednumber.DataLinkType;

public class PcapHandleFactory {
    public static PcapHandle createHandle(PcapEndpoint endpoint) throws Exception {
        if (endpoint.getPcapType() == PcapType.device) {
            PcapNetworkInterface nif = Pcaps.getDevByName(endpoint.getName());

            PromiscuousMode mode = endpoint.isPromiscuousMode() ? PromiscuousMode.PROMISCUOUS
                    : PromiscuousMode.NONPROMISCUOUS;

            PcapHandle handle = nif.openLive(endpoint.getSnapLength(), mode, endpoint.getTimeout());

            if(endpoint.getFilter() != null && endpoint.getFilter() != "") {
                handle.setFilter(endpoint.getFilter(), BpfCompileMode.OPTIMIZE);
            }                

            return handle;
        }

        return Pcaps.openDead(DataLinkType.LINUX_SLL, endpoint.getSnapLength());
    }

}
