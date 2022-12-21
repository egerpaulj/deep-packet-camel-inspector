package io.pkts.protocol;

import io.pkts.framer.Framer;
import io.pkts.framer.FramerManager;

/**
 * @author jonas@jonasborjesson.com
 */
public enum Protocol {
    ICMP("icmp", Layer.LAYER_3),
    ICMP6("icmp6", Layer.LAYER_3),
    IGMP("igmp", Layer.LAYER_3),
    TLS("tcp", Layer.LAYER_7),
    TCP("tcp", Layer.LAYER_4),
    UDP("udp", Layer.LAYER_4),
    SCTP("sctp", Layer.LAYER_4),
    SIP("sip", Layer.LAYER_7),
    SDP("sdp", Layer.LAYER_7),
    ETHERNET_II("eth", Layer.LAYER_2, 1L),
    SLL("sll", Layer.LAYER_2, 113L),
    IPv4("ip", Layer.LAYER_3, 101L),
    IPv6("ipv6", Layer.LAYER_3, 229L),
    PCAP("pcap", Layer.LAYER_1),
    RTP("rtp", Layer.LAYER_7),
    RTCP("rtcp", Layer.LAYER_7),
    ARP("arp", Layer.LAYER_3),
    UNKNOWN("unknown", null);

    private final String name;

    private final Layer layer;

    /** Nullable representation of the LINK-LAYER HEADER TYPE VALUES - see http://www.tcpdump.org/linktypes.html */
    private Long linkType = null;

    private Protocol(final String name, final Layer layer) {
        this.name = name;
        this.layer = layer;
    }

    private Protocol(final String name, final Layer layer, Long linkType) {
        this.name = name;
        this.layer = layer;
        this.linkType = linkType;
    }

    /**
     * The short name of this protocol. Similar to what Wireshark shows in its
     * short description of all the known protocols within its "super" frame.
     * E.g., if you "click" on the Pcap Frame it will have a field called
     * "protocols in frame" and will display something like
     * "eth:ip:udp:sip:sdp", this function will return a short name like that.
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Each {@link Protocol} operates within a particular {@link Layer} in the
     * ISO stack. This is mainly used by the {@link FramerManager} when trying
     * to determine which {@link Framer}s it should consult when trying to
     * figure out which framer to use.
     * 
     * @return
     */
    public Layer getProtocolLayer() {
        return this.layer;
    }

    /**
     * Get a protocol based on it's defined byte code. This is only true for
     * some protocols
     * 
     * For a full list: http://en.wikipedia.org/wiki/List_of_IP_protocol_numbers
     * 
     * @param code
     * @return
     */
    public static Protocol valueOf(final byte code) {
        switch (code) {
            case (byte) 0x01:
                return ICMP;
            case (byte) 0x02:
                return IGMP;
            case (byte) 0x06:
                return TCP;
            case (byte) 0x11:
                return UDP;
            case (byte) 0x84:
                return SCTP;
            case (byte) 0x3A:
                return ICMP6;
            default:
                return null;
        }
    }

    public Long getLinkType() {
        return linkType;
    }

    public static enum Layer {
        LAYER_1, LAYER_2, LAYER_3, LAYER_4, LAYER_7;
    }

}
