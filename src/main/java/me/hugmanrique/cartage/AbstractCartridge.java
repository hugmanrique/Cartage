package me.hugmanrique.cartage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemorySegment;

/**
 * Skeleton {@link Cartridge} implementation.
 */
public abstract class AbstractCartridge implements Cartridge {

  private final MemorySegment segment;

  private ByteOrder order;
  private long offset;

  protected AbstractCartridge(final MemorySegment segment, final ByteOrder order) {
    this.segment = requireNonNull(segment);
    this.order = requireNonNull(order);
    this.setOffset(0);
  }

  protected AbstractCartridge(final MemorySegment segment) {
    this(segment, ByteOrder.nativeOrder());
  }

  @Override
  public ByteOrder order() {
    return this.order;
  }

  @Override
  public void order(final ByteOrder order) {
    this.order = requireNonNull(order);
  }

  @Override
  public long offset() {
    return this.offset;
  }

  @Override
  public void setOffset(final long offset) {
    this.offset = Objects.checkIndex(offset, this.size());
  }

  @Override
  public long size() {
    return this.segment.byteSize();
  }

  @Override
  public long remaining() {
    return this.size() - this.offset;
  }

  @Override
  public boolean hasRemaining() {
    return this.offset < this.size();
  }

  @Override
  public void close() {
    if (!this.segment.isAlive()) {
      throw new IllegalStateException("This cartridge is already closed");
    }
    this.segment.close();
  }

  @Override
  public byte readByte() {
    byte value = this.getByte(this.offset);
    this.offset++;
    return value;
  }

  @Override
  public byte getByte(final long offset) {
    return MemoryAccess.getByteAtOffset(this.segment, offset);
  }

  @Override
  public int readUnsignedByte() {
    return Byte.toUnsignedInt(this.readByte());
  }

  @Override
  public int getUnsignedByte(final long offset) {
    return Byte.toUnsignedInt(this.getByte(this.offset));
  }

  @Override
  public void writeByte(final byte value) {
    this.setByte(this.offset, value);
    this.offset++;
  }

  @Override
  public void setByte(final long offset, final byte value) {
    MemoryAccess.setByteAtOffset(this.segment, offset, value);
  }

  @Override
  public void writeUnsignedByte(final int value) {
    this.writeByte((byte) (value & 0xFF));
  }

  @Override
  public void setUnsignedByte(final long offset, final int value) {
    this.setByte(offset, (byte) (value & 0xFF));
  }

  @Override
  public short readShort() {
    short value = this.getShort(this.offset);
    this.offset += Short.BYTES;
    return value;
  }

  @Override
  public short getShort(final long offset) {
    return MemoryAccess.getShortAtOffset(this.segment, offset, this.order);
  }

  @Override
  public int readUnsignedShort() {
    return Short.toUnsignedInt(this.readShort());
  }

  @Override
  public int getUnsignedShort(final long offset) {
    return Short.toUnsignedInt(this.getShort(offset));
  }

  @Override
  public void writeShort(final short value) {
    this.setShort(this.offset, value);
    this.offset += Short.BYTES;
  }

  @Override
  public void setShort(final long offset, final short value) {
    MemoryAccess.setShortAtOffset(this.segment, offset, this.order, value);
  }

  @Override
  public void writeUnsignedShort(final int value) {
    this.writeShort((short) (value & 0xFFFF));
  }

  @Override
  public void setUnsignedShort(final long offset, final int value) {
    this.setShort(offset, (short) (value & 0xFFFF));
  }

  @Override
  public int readInt() {
    int value = this.getInt(this.offset);
    this.offset += Integer.BYTES;
    return value;
  }

  @Override
  public int getInt(final long offset) {
    return MemoryAccess.getIntAtOffset(this.segment, offset, this.order);
  }

  @Override
  public long readUnsignedInt() {
    return Integer.toUnsignedLong(this.readInt());
  }

  @Override
  public long getUnsignedInt(final long offset) {
    return Integer.toUnsignedLong(this.getInt(offset));
  }

  @Override
  public void writeInt(final int value) {
    this.setInt(this.offset, value);
    this.offset += Integer.BYTES;
  }

  @Override
  public void setInt(final long offset, final int value) {
    MemoryAccess.setIntAtOffset(this.segment, offset, this.order, value);
  }

  @Override
  public void writeUnsignedInt(final long value) {
    this.writeInt((int) (value & 0xFFFFFFFFL));
  }

  @Override
  public void setUnsignedInt(final long offset, final long value) {
    this.setInt(offset, (int) (value & 0xFFFFFFFFL));
  }

  @Override
  public long readLong() {
    long value = this.getLong(this.offset);
    this.offset += Long.BYTES;
    return value;
  }

  @Override
  public long getLong(final long offset) {
    return MemoryAccess.getLongAtOffset(this.segment, offset, this.order);
  }

  @Override
  public void writeLong(final long value) {
    this.setLong(this.offset, value);
    this.offset += Long.BYTES;
  }

  @Override
  public void setLong(final long offset, final long value) {
    MemoryAccess.setLongAtOffset(this.segment, offset, this.order, value);
  }

  @Override
  public void readBytes(final byte[] dest) {
    this.getBytes(this.offset, dest, 0, dest.length);
    this.offset += dest.length;
  }

  @Override
  public void readBytes(final byte[] destArray, final int destOffset, final int length) {
    this.getBytes(this.offset, destArray, destOffset, length);
    this.offset += length;
  }

  @Override
  public void getBytes(final long offset, final byte[] dest) {
    this.getBytes(offset, dest, 0, dest.length);
  }

  @Override
  public void getBytes(final long offset, final byte[] destArray, final int destOffset,
                       final int length) {
    // TODO Avoid segment allocations
    try (var dest = MemorySegment.ofArray(destArray)) {
      dest.asSlice(destOffset, length).copyFrom(this.segment.asSlice(offset, length));
    }
  }

  @Override
  public void writeBytes(final byte[] source) {
    this.setBytes(this.offset, source, 0, source.length);
    this.offset += source.length;
  }

  @Override
  public void writeBytes(final byte[] source, final int sourceOffset, final int length) {
    this.setBytes(this.offset, source, sourceOffset, length);
    this.offset += length;
  }

  @Override
  public void setBytes(final long offset, final byte[] source) {
    this.setBytes(offset, source, 0, source.length);
  }

  @Override
  public void setBytes(final long offset, final byte[] sourceArray, final int sourceOffset,
                       final int length) {
    // TODO Avoid segment allocations
    try (var source = MemorySegment.ofArray(sourceArray)) {
      this.segment.asSlice(offset, length).copyFrom(source.asSlice(sourceOffset, length));
    }
  }

  /*@Override
  public void interCopy(final long sourceOffset, final long destOffset, final int length) {
    var source = this.segment.asSlice(destOffset, length);
    var dest = this.segment.asSlice(destOffset, length);
    source.copyFrom(dest);
  }*/

  @Override
  public String readString(final int length, final Charset charset) {
    String value = this.getString(this.offset, length, charset);
    this.offset += length;
    return value;
  }

  @Override
  public String getString(final long offset, final int length, final Charset charset) {
    requireNonNull(charset);
    byte[] encoded = new byte[length];
    this.getBytes(offset, encoded);
    return new String(encoded, charset);
  }

  @Override
  public String readAscii(final int length) {
    return this.readString(length, StandardCharsets.US_ASCII);
  }

  @Override
  public String getAscii(final long offset, final int length) {
    return this.getString(offset, length, StandardCharsets.US_ASCII);
  }

  @Override
  public String readUtf8(final int length) {
    return this.readString(length, StandardCharsets.UTF_8);
  }

  @Override
  public String getUtf8(final long offset, final int length) {
    return this.getString(offset, length, StandardCharsets.UTF_8);
  }

  @Override
  public int writeString(final CharSequence sequence, final Charset charset) {
    int written = this.setString(this.offset, sequence, charset);
    this.offset += written;
    return written;
  }

  @Override
  public int setString(final long offset, final CharSequence sequence, final Charset charset) {
    requireNonNull(sequence);
    requireNonNull(charset);
    byte[] encoded = sequence.toString().getBytes(charset);
    this.setBytes(offset, encoded);
    return encoded.length;
  }

  @Override
  public int writeAscii(final CharSequence sequence) {
    return this.writeString(sequence, StandardCharsets.US_ASCII);
  }

  @Override
  public int setAscii(final long offset, final CharSequence sequence) {
    return this.setString(offset, sequence, StandardCharsets.US_ASCII);
  }

  @Override
  public int writeUtf8(final CharSequence sequence) {
    return this.writeString(sequence, StandardCharsets.UTF_8);
  }

  @Override
  public int setUtf8(final long offset, final CharSequence sequence) {
    return this.setString(offset, sequence, StandardCharsets.UTF_8);
  }

  @Override
  public void copyFrom(final MemorySegment source) {
    this.segment.copyFrom(requireNonNull(source));
  }

  @Override
  public void copyTo(final MemorySegment dest) {
    requireNonNull(dest);
    dest.copyFrom(this.segment);
  }

  @Override
  public void copyTo(final Path path) throws IOException {
    requireNonNull(path);
    try (var dest =
           MemorySegment.mapFile(path, 0, this.size(), FileChannel.MapMode.READ_WRITE)) {
      dest.copyFrom(this.segment);
    }
  }

  @Override
  public void copyTo(final OutputStream stream) throws IOException {
    requireNonNull(stream);
    byte[] data = this.segment.toByteArray();
    stream.write(data);
  }

  @Override
  public String toString() {
    return "AbstractCartridge{"
      + "segment=" + segment
      + ", order=" + order
      + ", offset=" + offset
      + '}';
  }
}
