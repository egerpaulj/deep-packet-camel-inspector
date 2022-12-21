/**
 * 
 */
package io.pkts.packet.impl;

import io.pkts.buffer.Buffer;
import io.pkts.packet.Packet;
import io.pkts.packet.SDPPacket;
import io.pkts.packet.UDPPacket;
import io.pkts.packet.sip.SipPacket;
import io.pkts.protocol.Protocol;
import io.pkts.sdp.SDP;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author jonas@jonasborjesson.com
 */
public final class SDPPacketImpl extends AbstractPacket implements SDPPacket {

    private final Packet parent;
    private final SDP sdp;

    /**
     * 
     * @param parent
     *            an SDP will always be carried by some other protocol and the
     *            parent {@link Packet} is that protcol. Typically, this will be
     *            a {@link SipPacket} but could by a {@link UDPPacket} as well
     *            or just about anything really.
     * @param actualSDP
     *            the underlying actual SDP
     */
    public SDPPacketImpl(final Packet parent, final SDP actualSDP) {
        super(Protocol.SDP, parent, null);
        this.parent = parent;
        this.sdp = actualSDP;
    }

    @Override
    public long getArrivalTime() {
        return this.parent.getArrivalTime();
    }

    @Override
    public void verify() {
        // TODO Auto-generated method stub
    }

    @Override
    public Buffer getPayload() {
        // no payload for SDP's so return null
        return null;
    }

    @Override
    public Buffer toBuffer() {
        return this.sdp.toBuffer();
    }

    @Override
    public String toString() {
        return this.sdp.toString();
    }

    @Override
    public SDPPacket clone() {
        final Packet p = this.parent.clone();
        // TODO: clone
        return new SDPPacketImpl(p, this.sdp);
    }

    @Override
    public Packet getNextPacket() throws IOException {
        // No next packet for an SDP
        return null;
    }

    @Override
    public void write(final OutputStream out, final Buffer payload) throws IOException {
        // TODO Auto-generated method stub
        throw new RuntimeException("Haven't implemented this yet");
    }
}
