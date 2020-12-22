package me.hugmanrique.cartage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import me.hugmanrique.cartage.util.BufferUtils;

/**
 * Skeletal {@link Cartridge} implementation backed by a {@link ByteBuffer}.
 *
 * <p>Even if expensive for small sizes, this implementation supports
 * accessing memory-mapped files in {@link MapMode#READ_WRITE} mode.
 */
public abstract class AbstractCartridge implements Cartridge {

  private final ByteBuffer buffer;

  /**
   * Constructs a cartridge backed by the given buffer.
   *
   * @param buffer the buffer
   */
  protected AbstractCartridge(final ByteBuffer buffer) {
    this.buffer = requireNonNull(buffer);
  }

  /**
   * Constructs a cartridge with the given data.
   *
   * @param data the cartridge data
   */
  protected AbstractCartridge(final byte[] data) {
    this(ByteBuffer.wrap(requireNonNull(data)));
  }

  @Override
  public void writeTo(final Path path) throws IOException {
    requireNonNull(path);
    Files.write(path, BufferUtils.toByteArray(this.buffer));
  }

  @Override
  public void writeTo(final OutputStream stream) throws IOException {
    requireNonNull(stream);
    stream.write(BufferUtils.toByteArray(this.buffer));
  }

  @Override
  public byte readByte() {
    return this.buffer.get();
  }

  @Override
  public byte getByte(final int offset) {
    return this.buffer.get(offset);
  }

  @Override
  public int readUnsignedByte() {
    return this.readByte() & 0xFF;
  }

  @Override
  public int getUnsignedByte(final int offset) {
    return this.getByte(offset) & 0xFF;
  }

  @Override
  public void writeByte(final byte value) {
    this.buffer.put(value);
  }

  @Override
  public void setByte(final int offset, final byte value) {
    this.buffer.put(offset, value);
  }

  @Override
  public void writeUnsignedByte(final int value) {
    this.writeByte((byte) (value & 0xFF));
  }

  @Override
  public void setUnsignedByte(final int offset, final int value) {
    this.setByte(offset, (byte) (value & 0xFF));
  }

  @Override
  public short readShort() {
    return this.buffer.getShort();
  }

  @Override
  public short getShort(final int offset) {
    return this.buffer.getShort(offset);
  }

  @Override
  public int readUnsignedShort() {
    return this.readShort() & 0xFFFF;
  }

  @Override
  public int getUnsignedShort(final int offset) {
    return this.getShort(offset) & 0xFFFF;
  }

  @Override
  public void writeShort(final short value) {
    this.buffer.putShort(value);
  }

  @Override
  public void setShort(final int offset, final short value) {
    this.buffer.putShort(offset, value);
  }

  @Override
  public void writeUnsignedShort(final int value) {
    this.writeShort((short) (value & 0xFFFF));
  }

  @Override
  public void setUnsignedShort(final int offset, final int value) {
    this.setShort(offset, (short) (value & 0xFFFF));
  }

  @Override
  public int readInt() {
    return this.buffer.getInt();
  }

  @Override
  public int getInt(final int offset) {
    return this.buffer.getInt(offset);
  }

  @Override
  public long readUnsignedInt() {
    return (long) this.readInt() & 0xFFFFFFFFL;
  }

  @Override
  public long getUnsignedInt(final int offset) {
    return (long) this.getInt(offset) & 0xFFFFFFFFL;
  }

  @Override
  public void writeInt(final int value) {
    this.buffer.putInt(value);
  }

  @Override
  public void setInt(final int offset, final int value) {
    this.buffer.putInt(offset, value);
  }

  @Override
  public void writeUnsignedInt(final long value) {
    this.writeInt((int) (value & 0xFFFFFFFFL));
  }

  @Override
  public void setUnsignedInt(final int offset, final long value) {
    this.setInt(offset, (int) (value & 0xFFFFFFFFL));
  }

  @Override
  public long readLong() {
    return this.buffer.getLong();
  }

  @Override
  public long getLong(final int offset) {
    return this.buffer.getLong(offset);
  }

  @Override
  public void writeLong(final long value) {
    this.buffer.putLong(value);
  }

  @Override
  public void setLong(final int offset, final long value) {
    this.buffer.putLong(offset, value);
  }

  @Override
  public float readFloat() {
    return this.buffer.getFloat();
  }

  @Override
  public float getFloat(final int offset) {
    return this.buffer.getFloat(offset);
  }

  @Override
  public void writeFloat(final float value) {
    this.buffer.putFloat(value);
  }

  @Override
  public void setFloat(final int offset, final float value) {
    this.buffer.putFloat(offset, value);
  }

  @Override
  public double readDouble() {
    return this.buffer.getDouble();
  }

  @Override
  public double getDouble(final int offset) {
    return this.buffer.getDouble(offset);
  }

  @Override
  public void writeDouble(final double value) {
    this.buffer.putDouble(value);
  }

  @Override
  public void setDouble(final int offset, final double value) {
    this.buffer.putDouble(offset, value);
  }

  @Override
  public void readBytes(final byte[] dest) {
    this.buffer.get(dest);
  }

  @Override
  public void readBytes(final byte[] dest, final int destOffset, final int length) {
    this.buffer.get(dest, destOffset, length);
  }

  @Override
  public void getBytes(final int offset, final byte[] dest) {
    this.buffer.get(offset, dest);
  }

  @Override
  public void getBytes(final int offset,
                       final byte[] dest, final int destOffset, final int length) {
    this.buffer.get(offset, dest, destOffset, length);
  }

  @Override
  public void writeBytes(final byte[] source) {
    this.buffer.put(source);
  }

  @Override
  public void writeBytes(final byte[] source, final int sourceOffset, final int length) {
    this.buffer.put(source, sourceOffset, length);
  }

  @Override
  public void setBytes(final int offset, final byte[] source) {
    this.buffer.put(offset, source);
  }

  @Override
  public void setBytes(final int offset,
                       final byte[] source, final int sourceOffset, final int length) {
    this.buffer.put(offset, source, sourceOffset, length);
  }

  @Override
  public String readString(final int length, final Charset charset) {
    byte[] encoded = new byte[length];
    this.readBytes(encoded);
    return new String(encoded, charset);
  }

  @Override
  public String getString(final int offset, final int length, final Charset charset) {
    byte[] encoded = new byte[length];
    this.getBytes(offset, encoded);
    return new String(encoded, charset);
  }

  @Override
  public String readAscii(final int length) {
    return this.readString(length, StandardCharsets.US_ASCII);
  }

  @Override
  public String getAscii(final int offset, final int length) {
    return this.getString(offset, length, StandardCharsets.US_ASCII);
  }

  @Override
  public String readUtf8(final int length) {
    return this.readString(length, StandardCharsets.UTF_8);
  }

  @Override
  public String getUtf8(final int offset, final int length) {
    return this.getString(offset, length, StandardCharsets.UTF_8);
  }

  @Override
  public void writeString(final CharSequence value, final Charset charset) {
    requireNonNull(value);
    requireNonNull(charset);
    byte[] encoded = value.toString().getBytes(charset);
    this.writeBytes(encoded);
  }

  @Override
  public void setString(final int offset, final CharSequence value, final Charset charset) {
    requireNonNull(value);
    requireNonNull(charset);
    byte[] encoded = value.toString().getBytes(charset);
    this.setBytes(offset, encoded);
  }

  @Override
  public void writeAscii(final CharSequence value) {
    this.writeString(value, StandardCharsets.US_ASCII);
  }

  @Override
  public void setAscii(final int offset, final CharSequence value) {
    this.setString(offset, value, StandardCharsets.US_ASCII);
  }

  @Override
  public void writeUtf8(final CharSequence value) {
    this.writeString(value, StandardCharsets.UTF_8);
  }

  @Override
  public void setUtf8(final int offset, final CharSequence value) {
    this.setString(offset, value, StandardCharsets.UTF_8);
  }

  @Override
  public String toString() {
    return "AbstractCartridge{"
      + "buffer=" + buffer
      + '}';
  }
}
