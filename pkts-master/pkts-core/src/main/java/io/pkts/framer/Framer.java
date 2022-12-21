/**
 * 
 */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;

import java.io.IOException;

/**
 * Simple interface for framers.
 * 
 * @author jonas@jonasborjesson.com
 */
public interface Framer<P extends Packet, T extends Packet> {

    /**
     * 
     * @return the protocol this framer is capable of framing
     */
    Protocol getProtocol();

    /**
     * Ask the framer to frame the buffer into a packet. Note that the
     * {@link Packet} has not been parsed, just framed.
     * 
     * @param parent
     *            the parent frame or null if the frame doesn't have one.
     * @param buffer
     *            the buffer containing all the raw data
     * 
     * @return a new frame
     * @throws IOException
     *             in case something goes wrong when reading data from the
     *             buffer
     * @throws FramingException
     *             in case the data in buffer can't be parsed by the framer
     */
    T frame(P parent, Buffer buffer) throws IOException, FramingException;

    /**
     * Check whether the supplied data could be framed into a frame of this
     * type. Typically, a {@link Framer} will read a few bytes in an effort to
     * figure out if the incoming data may be something it recognizes. E.g., a
     * HTTP {@link Framer} could check if the first bytes match "GET", "POST",
     * "PUT" and "DELETE" if if so assume that the payload indeed is HTTP and
     * therefore return true. Of course, you may never really know until you
     * actually have parsed the entire body but this method should return quick
     * and if turns out that the data isn't what it thought, throw an exception
     * and the framework will take care of it.
     * 
     * @param data
     *            the data to check whether it could be a frame of this type
     * @return true if the data indeed could be of this framer type, false if
     *         not
     */
    boolean accept(Buffer data) throws IOException;

}
