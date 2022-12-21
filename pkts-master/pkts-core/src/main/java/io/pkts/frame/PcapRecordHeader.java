/**
 * 
 */
package io.pkts.frame;

import io.pkts.buffer.Buffer;
import io.pkts.buffer.Buffers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

/**
 * @author jonas@jonasborjesson.com
 * 
 */
public final class PcapRecordHeader {
    /**
     * pcaprec_hdr_s struct is SIZE bytes long.
     */
    public static final int SIZE = 16;

    private final ByteOrder byteOrder;

    private final Buffer body;

    private final boolean nsTimestamps;

    /**
     * 
     */
    public PcapRecordHeader(final ByteOrder byteOrder, final Buffer body) {
        this(byteOrder, body, false);
    }

    public PcapRecordHeader(final ByteOrder byteOrder, final Buffer body, final boolean nsTimestamps) {
        assert body != null;
        assert body.capacity() == SIZE;

        this.byteOrder = byteOrder;
        this.body = body;
        this.nsTimestamps = nsTimestamps;
    }

    /**
     * Create a default record header, which you must alter later on to match
     * whatever it is you are writing into the pcap stream.
     * 
     * @param timestamp
     *            the timestamp in milliseconds since epoch. This is the
     *            timestamp of when the packet "arrived"
     * 
     * @return
     */
    public static PcapRecordHeader createDefaultHeader(final long timestamp) {
        final byte[] body = new byte[SIZE];

        // time stamp seconds
        // body[0] = (byte) 0x00;
        // body[1] = (byte) 0x00;
        // body[2] = (byte) 0x00;
        // body[3] = (byte) 0x00;

        // Time stamp microseconds
        // body[4] = (byte) 0x00;
        // body[5] = (byte) 0x00;
        // body[6] = (byte) 0x00;
        // body[7] = (byte) 0x00;

        // The total length of the data
        // body[8] = (byte) 0x00;
        // body[9] = (byte) 0x00;
        // body[10] = (byte) 0x00;
        // body[11] = (byte) 0x00;

        // The length of the data captures within this frame
        // body[12] = (byte) 0x00;
        // body[13] = (byte) 0x00;
        // body[14] = (byte) 0x00;
        // body[15] = (byte) 0x00;

        final Buffer buffer = Buffers.wrap(body);
        buffer.setUnsignedInt(0, timestamp / 1000L);

        buffer.setUnsignedInt(4, timestamp % 1000L * 1000L);
        return new PcapRecordHeader(ByteOrder.LITTLE_ENDIAN, buffer);
    }

    // private static void setUnsignedInt(int index, )

    public long getTimeStampSeconds() {
        return PcapGlobalHeader.getUnsignedInt(0, this.body.getArray(), this.byteOrder);
    }

    @Deprecated
    public long getTimeStampMicroSeconds() {
        return PcapGlobalHeader.getUnsignedInt(4, this.body.getArray(), this.byteOrder);
    }

    public long getTimeStampMicroOrNanoSeconds() {
        return PcapGlobalHeader.getUnsignedInt(4, this.body.getArray(), this.byteOrder);
    }

    /**
     * Get the total length of the data. Not all of that data may have been
     * captured in this one frame, which is evident if the actual captured
     * length is different from the total length
     * 
     * @return
     */
    public long getTotalLength() {
        return PcapGlobalHeader.getUnsignedInt(12, this.body.getArray(), this.byteOrder);
    }

    public void setTotalLength(final long length) {
        this.body.setUnsignedInt(12, length);
    }

    /**
     * Get the actual length of what is contained in this frame.
     * 
     * @return the length in bytes
     */
    public long getCapturedLength() {
        return PcapGlobalHeader.getUnsignedInt(8, this.body.getArray(), this.byteOrder);
    }

    public void setCapturedLength(final long length) {
        this.body.setUnsignedInt(8, length);
    }

    public void write(final OutputStream out) throws IOException {
        out.write(this.body.getArray());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final long ts = getTimeStampSeconds();
        final long tsMicroOrNanoSeconds = getTimeStampMicroOrNanoSeconds();
        sb.append("ts_s: ").append(ts).append("\n");

        if (this.nsTimestamps) {
          sb.append("ts_ns: ");
        } else {
          sb.append("ts_us: ");
        }

        sb.append(tsMicroOrNanoSeconds).append("\n")
          .append("octects: ").append(getTotalLength()).append("\n")
          .append("length: ").append(getCapturedLength()).append("\n");

        return sb.toString();
    }
}
