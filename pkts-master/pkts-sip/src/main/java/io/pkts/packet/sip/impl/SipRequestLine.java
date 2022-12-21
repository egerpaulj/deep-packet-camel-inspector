/**
 * 
 */
package io.pkts.packet.sip.impl;

import io.pkts.buffer.Buffer;
import io.pkts.buffer.Buffers;
import io.pkts.packet.sip.SipParseException;
import io.pkts.packet.sip.address.SipURI;
import io.pkts.packet.sip.address.URI;

import java.io.IOException;

/**
 * Class representing a sip request line
 * 
 * @author jonas@jonasborjesson.com
 */
public final class SipRequestLine extends SipInitialLine {

    private final Buffer method;
    private final Buffer requestUriBuffer;

    /**
     * The parsed request uri, which may be null if no one has asked about it
     * yet.
     */
    private URI requestURI;

    public SipRequestLine(final Buffer method, final Buffer requestUri) {
        super();
        assert method != null;
        assert requestUri != null;
        this.method = method;
        this.requestUriBuffer = requestUri;
    }

    public SipRequestLine(final Buffer method, final URI requestUri) {
        super();
        assert method != null;
        assert requestUri != null;
        this.method = method;
        this.requestUriBuffer = null;
        this.requestURI = requestUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRequestLine() {
        return true;
    }

    @Override
    public SipRequestLine toRequestLine() {
        return this;
    }

    public Buffer getMethod() {
        return this.method;
    }

    public URI getRequestUri() throws SipParseException {
        if (this.requestURI == null) {
            try {
                this.requestURI = SipURI.frame(this.requestUriBuffer);
            } catch (final IOException e) {
                throw new SipParseException(0, "Unable to parse the request uri", e);
            }
        }
        return this.requestURI;
    }

    /**
     * Two {@link SipRequestLine}s are considered equal if:
     * <ul>
     *     <li>The methods are the same</li>
     *     <li>The request-uri are considered equal</li>
     * </ul>
     * @param o
     * @return
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        try {
            final SipRequestLine other = (SipRequestLine)o;
            if (!method.equals(other.method)) {
                return false;
            }
            return getRequestUri().equals(other.getRequestUri());
        } catch (final SipParseException | ClassCastException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Buffer getBuffer() {
        final Buffer tmp = Buffers.createBuffer(1024);
        getBytes(tmp);
        return tmp;
    }

    @Override
    public String toString() {
        return getBuffer().toString();
    }

    @Override
    public SipRequestLine clone() {
        final Buffer requestURI = this.requestUriBuffer.clone();
        return new SipRequestLine(this.method, requestURI);
    }

    @Override
    public void getBytes(final Buffer dst) {
        this.method.getBytes(0, dst);
        dst.write(SipParser.SP);
        if (this.requestURI != null) {
            this.requestURI.getBytes(dst);
        } else {
            this.requestUriBuffer.getBytes(0, dst);
        }
        dst.write(SipParser.SP);
        SipParser.SIP2_0.getBytes(0, dst);
    }
}
