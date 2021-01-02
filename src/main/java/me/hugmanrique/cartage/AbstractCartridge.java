package me.hugmanrique.cartage;

import static java.util.Objects.requireNonNull;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Objects;
import jdk.incubator.foreign.MemorySegment;

/**
 * Skeleton {@link Cartridge} implementation.
 */
public abstract class AbstractCartridge implements Cartridge {

  private final MemorySegment segment;
  private ByteOrder order;

  private long offset;
  private final long size;

  public AbstractCartridge(final byte[] data) {
    requireNonNull(data);
    this.segment = MemorySegment.ofArray(data);
    this.order = ByteOrder.nativeOrder(); // TODO Specify default behavior in javadoc
    this.size = this.segment.byteSize();
    this.setOffset(0);
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

  private void checkOffset(final int offset, final int length) {
    if (this.size - offset < length) {
      throw new IndexOutOfBoundsException(
        String.format("Cannot read %d bytes at %d, only %d remaining",
                      length, offset, this.size - offset));
    }
  }

  @Override
  public void setOffset(final long offset) {
    this.offset = Objects.checkIndex(offset, this.size);
  }

  @Override
  public long size() {
    return this.size;
  }

  @Override
  public long remaining() {
    return this.size - this.offset;
  }

  @Override
  public boolean hasRemaining() {
    return this.offset < this.size;
  }

  @Override
  public void close() {
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
    return 0;
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

  }

  @Override
  public void writeUnsignedByte(final int value) {

  }

  @Override
  public void setUnsignedByte(final long offset, final int value) {

  }

  @Override
  public short readShort() {
    return 0;
  }

  @Override
  public short getShort(final long offset) {
    return 0;
  }

  @Override
  public void writeShort(final short value) {

  }

  @Override
  public void setShort(final long offset, final short value) {

  }

  @Override
  public int readInt() {
    return 0;
  }

  @Override
  public int getInt(final long offset) {
    return 0;
  }

  @Override
  public long readUnsignedInt() {
    return 0;
  }

  @Override
  public long getUnsignedInt(final long offset) {
    return 0;
  }

  @Override
  public void writeInt(final int value) {

  }

  @Override
  public void setInt(final long offset, final int value) {

  }

  @Override
  public void writeUnsignedInt(final long value) {

  }

  @Override
  public void setUnsignedInt(final long offset, final long value) {

  }

  @Override
  public long readLong() {
    return 0;
  }

  @Override
  public long getLong(final long offset) {
    return 0;
  }

  @Override
  public void writeLong(final long value) {

  }

  @Override
  public void setLong(final long offset, final long value) {

  }

  @Override
  public void readBytes(final byte[] dest) {

  }

  @Override
  public void readBytes(final byte[] dest, final int destOffset, final int length) {

  }

  @Override
  public void getBytes(final long offset, final byte[] dest) {

  }

  @Override
  public void getBytes(final long offset, final byte[] dest, final int destOffset, final int length) {

  }

  @Override
  public void writeBytes(final byte[] source) {

  }

  @Override
  public void writeBytes(final byte[] source, final int sourceOffset, final int length) {

  }

  @Override
  public void setBytes(final long offset, final byte[] source) {

  }

  @Override
  public void setBytes(final long offset, final byte[] source, final int sourceOffset, final int length) {

  }

  @Override
  public String readString(final int length, final Charset charset) {
    return null;
  }

  @Override
  public String getString(final long offset, final int length, final Charset charset) {
    return null;
  }

  @Override
  public String readAscii(final int length) {
    return null;
  }

  @Override
  public String getAscii(final long offset, final int length) {
    return null;
  }

  @Override
  public String readUtf8(final int length) {
    return null;
  }

  @Override
  public String getUtf8(final long offset, final int length) {
    return null;
  }

  @Override
  public int writeString(final CharSequence sequence, final Charset charset) {
    return 0;
  }

  @Override
  public int setString(final long offset, final CharSequence sequence, final Charset charset) {
    return 0;
  }

  @Override
  public int writeAscii(final CharSequence sequence) {
    return 0;
  }

  @Override
  public int setAscii(final long offset, final CharSequence sequence) {
    return 0;
  }

  @Override
  public int writeUtf8(final CharSequence sequence) {
    return 0;
  }

  @Override
  public int setUtf8(final long offset, final CharSequence sequence) {
    return 0;
  }
}
