/**
 * 
 */
package io.pkts.packet.sip.header;

import io.pkts.buffer.Buffer;
import io.pkts.buffer.Buffers;
import io.pkts.packet.sip.SipParseException;
import io.pkts.packet.sip.address.Address;
import io.pkts.packet.sip.header.impl.FromHeaderImpl;

import java.util.Random;


/**
 * Source: RFC 3261 section 8.1.1.3
 * 
 * <p>
 * The From header field indicates the logical identity of the initiator of the
 * request, possibly the user's address-of-record. Like the To header field, it
 * contains a URI and optionally a display name. It is used by SIP elements to
 * determine which processing rules to apply to a request (for example,
 * automatic call rejection). As such, it is very important that the From URI
 * not contain IP addresses or the FQDN of the host on which the UA is running,
 * since these are not logical names.
 * </p>
 * 
 * <p>
 * The From header field allows for a display name. A UAC SHOULD use the display
 * name "Anonymous", along with a syntactically correct, but otherwise
 * meaningless URI (like sip:thisis@anonymous.invalid), if the identity of the
 * client is to remain hidden.
 * </p>
 * 
 * <p>
 * Usually, the value that populates the From header field in requests generated
 * by a particular UA is pre-provisioned by the user or by the administrators of
 * the user's local domain. If a particular UA is used by multiple users, it
 * might have switchable profiles that include a URI corresponding to the
 * identity of the profiled user. Recipients of requests can authenticate the
 * originator of a request in order to ascertain that they are who their From
 * header field claims they are (see Section 22 for more on authentication).
 * </p>
 * 
 * <p>
 * The From field MUST contain a new "tag" parameter, chosen by the UAC. See
 * Section 19.3 for details on choosing a tag.
 * </p>
 * 
 * <p>
 * For further information on the From header field, see Section 20.20.
 * Examples:
 * </p>
 * 
 * <p>
 * 
 * <pre>
 *    From: "Bob" &lt;sips:bob@biloxi.com&gt;tag=a48s
 *    From: sip:+12125551212@phone2net.com;tag=887s
 *    From: Anonymous &lt;sip:c8oqz84zk7z@privacy.org&gt;;tag=hyh8
 * </pre>
 * 
 * </p>
 * 
 * @author jonas@jonasborjesson.com
 */
public interface FromHeader extends AddressParametersHeader {

    Buffer NAME = Buffers.wrap("From");

    Buffer COMPACT_NAME = Buffers.wrap("f");

    /**
     * Get the tag parameter.
     * 
     * @return the tag or null if it hasn't been set.
     * @throws SipParseException
     *             in case anything goes wrong while extracting tag.
     */
    Buffer getTag() throws SipParseException;

    @Override
    FromHeader clone();

    @Override
    default boolean isFromHeader() {
        return true;
    }

    @Override
    default FromHeader toFromHeader() {
        return this;
    }

    /**
     * Frame the value as a {@link FromHeader}.
     *
     * @param buffer
     * @return
     * @throws SipParseException in case anything goes wrong while parsing.
     */
    static FromHeader frame(final Buffer buffer) throws SipParseException {
        final Buffer original = buffer.slice();
        final Object[] result = AddressParametersHeader.frame(buffer);
        return new FromHeaderImpl(original, (Address) result[0], (Buffer) result[1]);
    }

    /**
     * Frame the value as a {@link FromHeader}.
     *
     * @param buffer
     * @return
     * @throws SipParseException in case anything goes wrong while parsing.
     */
    static FromHeader frame(final String buffer) throws SipParseException {
        return frame(Buffers.wrap(buffer));
    }

    /**
     * Generate a new tag that can be used as a tag parameter for the {@link FromHeader}. A
     * tag-parameter only has to be unique within the same Call-ID space so therefore it doesn't
     * have to be cryptographically strong etc.
     * 
     * @return
     */
    static Buffer generateTag() {
        // TODO: fix this and move it to a better place.
        return Buffers.wrap(Integer.toHexString(new Random().nextInt()));
    }

    static AddressParametersHeader.Builder<FromHeader> builder() {
        return new Builder();
    }

    static AddressParametersHeader.Builder<FromHeader> withHost(final Buffer host) throws SipParseException {
        return builder().withHost(host);
    }

    static AddressParametersHeader.Builder<FromHeader> withHost(final String host) throws SipParseException {
        return builder().withHost(host);
    }

    static AddressParametersHeader.Builder<FromHeader> withAddress(final Address address) throws SipParseException {
        return builder().withAddress(address);
    }

    @Override
    AddressParametersHeader.Builder<FromHeader> copy();

    class Builder extends AddressParametersHeader.Builder<FromHeader> {

        private Builder() {
            super(NAME);
        }

        @Override
        public FromHeader internalBuild(final Buffer rawValue, final Address address, final Buffer params) throws SipParseException {
            return new FromHeaderImpl(rawValue, address, params);
        }
    }

}
