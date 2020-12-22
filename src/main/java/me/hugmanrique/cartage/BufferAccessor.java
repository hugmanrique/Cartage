package me.hugmanrique.cartage;

import java.nio.charset.Charset;

/**
 * Provides buffer primitive data accessor methods.
 */
public interface BufferAccessor {

  // TODO offset(), reset(), etc.
  // TODO Remove unsigned utilities?

  // Primitives

  byte readByte();

  byte getByte(final int offset);

  int readUnsignedByte();

  int getUnsignedByte(final int offset);

  void writeByte(final byte value);

  void setByte(final int offset, final byte value);

  void writeUnsignedByte(final int value);

  void setUnsignedByte(final int offset, final int value);

  short readShort();

  short getShort(final int offset);

  int readUnsignedShort();

  int getUnsignedShort(final int offset);

  void writeShort(final short value);

  void setShort(final int offset, final short value);

  void writeUnsignedShort(final int value);

  void setUnsignedShort(final int offset, final int value);

  int readInt();

  int getInt(final int offset);

  long readUnsignedInt();

  long getUnsignedInt(final int offset);

  void writeInt(final int value);

  void setInt(final int offset, final int value);

  void writeUnsignedInt(final long value);

  void setUnsignedInt(final int offset, final long value);

  long readLong();

  long getLong(final int offset);

  void writeLong(final long value);

  void setLong(final int offset, final long value);

  // TODO Document these are accessed as IEEE 754 floating points

  float readFloat();

  float getFloat(final int offset);

  void writeFloat(final float value);

  void setFloat(final int offset, final float value);

  double readDouble();

  double getDouble(final int offset);

  void writeDouble(final double value);

  void setDouble(final int offset, final double value);

  // Byte arrays

  void readBytes(final byte[] dest);

  void readBytes(final byte[] dest, int destOffset, int length);

  void getBytes(final int offset, final byte[] dest);

  void getBytes(final int offset, final byte[] dest, final int destOffset, final int length);

  void writeBytes(final byte[] source);

  void writeBytes(final byte[] source, int sourceOffset, final int length);

  void setBytes(final int offset, final byte[] source);

  void setBytes(final int offset, final byte[] source, final int sourceOffset, final int length);

  // Strings

  // TODO
  String readString(final int length, final Charset charset);

  String getString(final int offset, final int length, final Charset charset);

  String readAscii(final int length);

  String getAscii(final int offset, final int length);

  String readUtf8(final int length);

  String getUtf8(final int offset, final int length);

  void writeString(final CharSequence value, final Charset charset);

  void setString(final int offset, final CharSequence value, final Charset charset);

  void writeAscii(final CharSequence value);

  void setAscii(final int offset, final CharSequence value);

  void writeUtf8(final CharSequence value);

  void setUtf8(final int offset, final CharSequence value);
}
