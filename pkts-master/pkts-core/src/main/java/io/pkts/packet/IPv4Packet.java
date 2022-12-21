/**
 *
 */
package io.pkts.packet;

/**
 * Represents a packet from the Network Layer (layer 3). Actually, to be
 * completely honest, the model implemented (at least so far) is more geared
 * towards what is commonly referred to as the Internet Layer and is strictly
 * speaking not quite the same as the Network Layer as specified by the OSI
 * model. However, until it becomes an issue this little "issue" is going to be
 * ignored and for now the Network Layer is equal to the Internet Layer.
 *
 * The current version of pkts.io is focused on IP anyway so...
 *
 * @author jonas@jonasborjesson.com
 */
public interface IPv4Packet extends IPPacket {
    /**
     * Setting an IPv4 address the fast way! Specify each part separately. E.g.,
     * setting 192.168.0.100 would be accomplished like so:
     *
     * {@link #setSourceIP(192, 168, 0, 100)}
     *
     * @param a
     *            the first part of the IPv4 address, e.g. 192
     * @param b
     *            the second part of the IPv4 address, e.g. 168
     * @param c
     *            the third part of the IPv4 address, e.g. 0
     * @param d
     *            the fourth part of the IPv4 address, e.g. 100
     */
    void setSourceIP(int a, int b, int c, int d);

    /**
     * Setting an IPv4 address the fast(est?) way! Specify each part separately.
     * E.g., setting 192.168.0.100 would be accomplished like so:
     *
     * @param rawIp
     */
    void setSourceIP(byte a, byte b, byte c, byte d);

    /**
     * Set the source IP of this {@link IPv4Packet}. Note, using
     * {@link #setSourceIP(int, int, int, int)} will be must faster so try and
     * use it instead.
     *
     * @param sourceIp
     */
    void setSourceIP(String sourceIp);

    /**
     * Setting an IPv4 address the fast way! Specify each part separately. E.g.,
     * setting 192.168.0.100 would be accomplished like so:
     *
     * {@link #setSourceIP(192, 168, 0, 100)}
     *
     * @param a
     *            the first part of the IPv4 address, e.g. 192
     * @param b
     *            the second part of the IPv4 address, e.g. 168
     * @param c
     *            the third part of the IPv4 address, e.g. 0
     * @param d
     *            the fourth part of the IPv4 address, e.g. 100
     */
    void setDestinationIP(int a, int b, int c, int d);

    void setDestinationIP(byte a, byte b, byte c, byte d);

    /**
     * Set the destination IP of this {@link IPv4Packet}. Note, using
     * {@link #setDestinationIP(int, int, int, int)} will be must faster so try
     * and use it instead.
     *
     * @param sourceIp
     */
    void setDestinationIP(String destinationIP);

    /**
     * The checksum of the IP-packet. The checksum in an IP-packet is a 16 bit
     * checksum of the header bytes (which the checksum set to zero) and is
     * returned as a unsigned short (hence an int)
     *
     * Checkout
     *
     * @return
     */
    int getIpChecksum();

    /**
     * After you change anything in an IP packet (apart from the payload) you
     * should re-calculate the checksum. If you don't, if this then is written
     * to a pcap and later opened in e.g. wireshark, then all packets will be
     * flagged as bad checksums.
     */
    void reCalculateChecksum();

    boolean verifyIpChecksum();

    @Override
    IPv4Packet clone();

    /**
     *
     * @return
     */
    boolean isFragmented();

    /**
         * The Reserved flag is part of the three-bit flag field and those flags
         * are: (in order, from high order to low order):
         *
         * <pre>
         * bit 0: Reserved; must be zero.
         * bit 1: Don't Fragment (DF)
         * bit 2: More Fragments (MF)
         * </pre>
         *
         * (source http://en.wikipedia.org/wiki/IPv4)
         *
         * @return should always return false
         */
    boolean isReservedFlagSet();

    /**
         * The DF flag is part of the three-bit flag field and those flags are: (in
         * order, from high order to low order):
         *
         * <pre>
         * bit 0: Reserved; must be zero.
         * bit 1: Don't Fragment (DF)
         * bit 2: More Fragments (MF)
         * </pre>
         *
         * If the DF flag is set, and fragmentation is required to route the packet,
         * then the packet is dropped. This can be used when sending packets to a
         * host that does not have sufficient resources to handle fragmentation. It
         * can also be used for Path MTU Discovery, either automatically by the host
         * IP software, or manually using diagnostic tools such as ping or
         * traceroute. For unfragmented packets, the MF flag is cleared. For
         * fragmented packets, all fragments except the last have the MF flag set.
         * The last fragment has a non-zero Fragment Offset field, differentiating
         * it from an unfragmented packet.
         *
         * (source http://en.wikipedia.org/wiki/IPv4)
         *
         * @return
         */
    boolean isDontFragmentSet();

    /**
         * The MF flag is part of the three-bit flag field and those flags are: (in
         * order, from high order to low order):
         *
         * <pre>
         * bit 0: Reserved; must be zero.
         * bit 1: Don't Fragment (DF)
         * bit 2: More Fragments (MF)
         * </pre>
         *
         * If the DF flag is set, and fragmentation is required to route the packet,
         * then the packet is dropped. This can be used when sending packets to a
         * host that does not have sufficient resources to handle fragmentation. It
         * can also be used for Path MTU Discovery, either automatically by the host
         * IP software, or manually using diagnostic tools such as ping or
         * traceroute. For unfragmented packets, the MF flag is cleared. For
         * fragmented packets, all fragments except the last have the MF flag set.
         * The last fragment has a non-zero Fragment Offset field, differentiating
         * it from an unfragmented packet.
         *
         * (source http://en.wikipedia.org/wiki/IPv4)
         *
         * @return
         */
    boolean isMoreFragmentsSet();

    /**
     * The fragment offset field, measured in units of eight-byte blocks, is 13
     * bits long and specifies the offset of a particular fragment relative to
     * the beginning of the original unfragmented IP datagram. The first
     * fragment has an offset of zero. This allows a maximum offset of (213 – 1)
     * × 8 = 65,528 bytes, which would exceed the maximum IP packet length of
     * 65,535 bytes with the header length included (65,528 + 20 = 65,548
     * bytes).
     *
     * (source http://en.wikipedia.org/wiki/IPv4)
     *
     * @return
     */
    short getFragmentOffset();
}
