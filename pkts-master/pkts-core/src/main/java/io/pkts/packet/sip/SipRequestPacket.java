/**
 * 
 */
package io.pkts.packet.sip;

import io.pkts.packet.sip.address.URI;

/**
 * @author jonas@jonasborjesson.com
 */
public interface SipRequestPacket extends SipPacket {

    /**
     * Get the request uri of the sip request
     * 
     * @return
     */
    URI getRequestUri() throws SipPacketParseException;

    @Override
    SipRequestPacket clone();

}
