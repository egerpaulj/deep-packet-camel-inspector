/**
 * 
 */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.sip.SipMessage;
import io.pkts.packet.sip.SipPacket;
import io.pkts.packet.sip.impl.SipParser;
import io.pkts.packet.sip.impl.SipRequestPacketImpl;
import io.pkts.packet.sip.impl.SipResponsePacketImpl;
import io.pkts.protocol.Protocol;

import java.io.IOException;

/**
 * @author jonas@jonasborjesson.com
 * 
 */
public final class SIPFramer implements Framer<TransportPacket, SipPacket> {

    public SIPFramer() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Protocol getProtocol() {
        return Protocol.SIP;
    }

    /**
     * {@inheritDoc}
     * 
     * Very basic way of framing a sip message. It makes a lot of assumption but
     * in the framing phase we are, well, just framing.
     */
    @Override
    public SipPacket frame(final TransportPacket parent, final Buffer buffer) throws IOException {

        if (parent == null) {
            throw new IllegalArgumentException("The parent frame cannot be null");
        }

        final SipMessage sip = SipParser.frame(buffer);
        if (sip.isRequest()) {
            return new SipRequestPacketImpl(parent, sip.toRequest());
        }

        return new SipResponsePacketImpl(parent, sip.toResponse());
    }

    @Override
    public boolean accept(final Buffer data) throws IOException {
        return couldBeSipMessage(data);
    }

    /**
     * Helper function that checks whether or not the data could be a SIP message. It is a very
     * basic check but if it doesn't go through it definitely is not a SIP message.
     * 
     * @param data
     * @return
     */
    public static boolean couldBeSipMessage(final Buffer data) throws IOException {
        if (data.getReadableBytes() < 4) {
            return false;
        }

        final byte a = data.getByte(0);
        final byte b = data.getByte(1);
        final byte c = data.getByte(2);
        final byte d = data.getByte(3);
        return a == 'S' && b == 'I' && c == 'P'              || // response
               a == 'I' && b == 'N' && c == 'V' && d == 'I' || // INVITE
               a == 'A' && b == 'C' && c == 'K'             || // ACK
               a == 'B' && b == 'Y' && c == 'E'             || // BYE
               a == 'O' && b == 'P' && c == 'T' && d == 'I' || // OPTIONS
               a == 'C' && b == 'A' && c == 'N' && d == 'C' || // CANCEL
               a == 'M' && b == 'E' && c == 'S' && d == 'S' || // MESSAGE
               a == 'R' && b == 'E' && c == 'G' && d == 'I' || // REGISTER
               a == 'I' && b == 'N' && c == 'F' && d == 'O' || // INFO
               a == 'P' && b == 'R' && c == 'A' && d == 'C' || // PRACK
               a == 'S' && b == 'U' && c == 'B' && d == 'S' || // SUBSCRIBE
               a == 'N' && b == 'O' && c == 'T' && d == 'I' || // NOTIFY
               a == 'U' && b == 'P' && c == 'D' && d == 'A' || // UPDATE
               a == 'R' && b == 'E' && c == 'F' && d == 'E' || // REFER
               a == 'P' && b == 'U' && c == 'B' && d == 'L' ; // PUBLISH

    }

}
