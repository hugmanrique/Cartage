package me.hugmanrique.cartage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Contains the data accessor methods that {@link Cartridge} provides.
 */
interface CartridgeAccessors {

  /**
   * Reads the byte at the cartridge's current offset, and then increments the offset.
   *
   * @return the byte value at the current offset
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is 0
   */
  byte readByte();

  /**
   * Reads the byte at the given offset.
   *
   * @param offset the offset to read from
   * @return the byte value at the given offset
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than or equal to {@link Cartridge#size()}
   */
  byte getByte(final long offset);

  /**
   * Reads the unsigned byte at the cartridge's current offset, and then increments the offset.
   *
   * @return the unsigned byte value at the current offset
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is 0
   */
  int readUnsignedByte();

  /**
   * Reads the unsigned byte at the given offset.
   *
   * @param offset the offset to read from
   * @return the unsigned byte value at the given offset
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than or equal to {@link Cartridge#size()}
   */
  int getUnsignedByte(final long offset);

  /**
   * Writes the given byte at the cartridge's current offset, and then increments the offset.
   *
   * @param value the byte value to write
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is 0
   */
  void writeByte(final byte value);

  /**
   * Writes the given byte at the given offset.
   *
   * @param offset the offset to write to
   * @param value the byte value to write
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than or equal to {@link Cartridge#size()}
   */
  void setByte(final long offset, final byte value);

  /**
   * Writes the given unsigned byte at the cartridge's current offset, and
   * then increments the offset.
   *
   * @param value the unsigned byte value to write
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is 0
   */
  void writeUnsignedByte(final int value);

  /**
   * Writes the given unsigned byte at the given offset.
   *
   * @param offset the offset to write to
   * @param value the byte value to write
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than or equal to {@link Cartridge#size()}
   */
  void setUnsignedByte(final long offset, final int value);

  /**
   * Reads the short at the cartridge's current offset, and then increments
   * the offset by {@link Short#BYTES}.
   *
   * @return the short value at the current offset
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Short#BYTES}.
   */
  short readShort();

  /**
   * Reads the short at the given offset.
   *
   * @param offset the offset to read from
   * @return the short value at the given offset
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Short#BYTES})}
   */
  short getShort(final long offset);

  /**
   * Reads the unsigned short at the cartridge's current offset, and then increments the offset by
   * {@link Short#BYTES}.
   *
   * @return the unsigned short value at the current offset
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Short#BYTES}.
   */
  int readUnsignedShort();

  /**
   * Reads the unsigned short at the given offset.
   *
   * @param offset the offset to read from
   * @return the unsigned short value at the given offset
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code {@link Cartridge#size()} - {@link Short#BYTES}}
   */
  int getUnsignedShort(final long offset);

  /**
   * Writes the given short at the cartridge's current offset, and then increments
   * the offset by {@link Short#BYTES}.
   *
   * @param value the short value to write
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Short#BYTES}
   */
  void writeShort(final short value);

  /**
   * Writes the given short at the given offset.
   *
   * @param offset the offset to write to
   * @param value the short value to write
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Short#BYTES})}
   */
  void setShort(final long offset, final short value);

  /**
   * Writes the given unsigned short at the cartridge's current offset, and then increments
   * the offset by {@link Short#BYTES}.
   *
   * @param value the unsigned short value to write
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Short#BYTES}
   */
  void writeUnsignedShort(final int value);

  /**
   * Writes the given unsigned short at the given offset.
   *
   * @param offset the offset to write to
   * @param value the unsigned short value to write
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Short#BYTES})}
   */
  void setUnsignedShort(final long offset, final int value);

  /**
   * Reads the int at the cartridge's current offset, and then increments
   * the offset by {@link Integer#BYTES}.
   *
   * @return the int value at the current offset
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Integer#BYTES}.
   */
  int readInt();

  /**
   * Reads the int at the given offset.
   *
   * @param offset the offset to read from
   * @return the int value at the given offset
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Integer#BYTES})}
   */
  int getInt(final long offset);

  /**
   * Reads the unsigned int at the cartridge's current offset, and then increments
   * the offset by {@link Integer#BYTES}.
   *
   * @return the unsigned int value at the current offset
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Integer#BYTES}.
   */
  long readUnsignedInt();

  /**
   * Reads the unsigned int at the given offset.
   *
   * @param offset the offset to read from
   * @return the unsigned int value at the given offset
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Integer#BYTES})}
   */
  long getUnsignedInt(final long offset);

  /**
   * Writes the given int at the cartridge's current offset, and then increments
   * the offset by {@link Integer#BYTES}.
   *
   * @param value the int value to write
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Integer#BYTES}
   */
  void writeInt(final int value);

  /**
   * Writes the given int at the given offset.
   *
   * @param offset the offset to write to
   * @param value the int value to write
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Integer#BYTES})}
   */
  void setInt(final long offset, final int value);

  /**
   * Writes the given unsigned int at the cartridge's current offset, and then increments
   * the offset by {@link Integer#BYTES}.
   *
   * @param value the unsigned int value to write
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Integer#BYTES}
   */
  void writeUnsignedInt(final long value);

  /**
   * Writes the given unsigned int at the given offset.
   *
   * @param offset the offset to write to
   * @param value the unsigned int value to write
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Integer#BYTES})}
   */
  void setUnsignedInt(final long offset, final long value);

  /**
   * Reads the long at the cartridge's current offset, and then increments
   * the offset by {@link Long#BYTES}.
   *
   * @return the long value at the current offset
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Long#BYTES}.
   */
  long readLong();

  /**
   * Reads the long at the given offset.
   *
   * @param offset the offset to read from
   * @return the long value at the given offset
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Long#BYTES})}
   */
  long getLong(final long offset);

  /**
   * Writes the given long at the cartridge's current offset, and then increments
   * the offset by {@link Long#BYTES}.
   *
   * @param value the long value to write
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@link Long#BYTES}
   */
  void writeLong(final long value);

  /**
   * Writes the given long at the given offset.
   *
   * @param offset the offset to write to
   * @param value the long value to write
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - {@link Long#BYTES})}
   */
  void setLong(final long offset, final long value);

  /**
   * Transfers bytes from the cartridge into the given array, beginning at the cartridge's
   * current offset, which is then incremented by {@code dest.length}.
   *
   * @param dest the destination array
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@code dest.length}
   */
  void readBytes(final byte[] dest);

  /**
   * Transfers {@code length} bytes from the cartridge into the given array,
   * beginning at the cartridge's current offset and at the given offset in the array.
   * The cartridge's offset is then incremented by {@code length}.
   *
   * @param dest the destination array
   * @param destOffset the offset within the array of the first byte to be written
   * @param length the number of bytes to be written
   * @throws IndexOutOfBoundsException if {@code destOffset} is out of bounds, i.e. less than 0 or
   *         greater than {@code dest.length - destOffset}; or {@link Cartridge#remaining()} is
   *         less than {@code length}
   */
  void readBytes(final byte[] dest, final int destOffset, final int length);

  /**
   * Transfers bytes from the cartridge into the given array, beginning at
   * the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge from which the first byte will be read
   * @param dest the destination array
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - dest.length)}
   */
  void getBytes(final long offset, final byte[] dest);

  /**
   * Transfers {@code length} bytes from the cartridge into the given array,
   * beginning at the given offset in the cartridge and at the given offset in the array.
   *
   * @param offset the offset in the cartridge from which the first byte will be read
   * @param dest the destination array
   * @param destOffset the offset within the array of the first byte to be written
   * @param length the number of bytes to be written
   * @throws IndexOutOfBoundsException if {@code offset} is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - length)}; or {@code destOffset}
   *         is out of bounds, i.e. less than 0 or greater than {@code dest.length - length}; or
   *         {@link Cartridge#remaining()} is less than {@code length}
   */
  void getBytes(final long offset, final byte[] dest, final int destOffset, final int length);

  /**
   * Transfers the entire content of the given array into the cartridge, beginning at
   * the cartridge's current offset, which is then incremented by {@code source.length}.
   *
   * @param source the source array
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than
   *         {@code source.length}
   */
  void writeBytes(final byte[] source);

  /**
   * Transfers {@code length} bytes from the given array into the cartridge, beginning at
   * the cartridge's current offset and at the given offset in the array.
   * The cartridge's offset is then incremented by {@code length}.
   *
   * @param source the source array
   * @param sourceOffset the offset within the array of the first byte to be read
   * @param length the number of bytes to be written
   * @throws IndexOutOfBoundsException if {@code sourceOffset} is out of bounds, i.e. less than 0 or
   *         greater than {@code (source.length - sourceOffset)}; or {@link Cartridge#remaining()}
   *         is less than {@code length}
   */
  void writeBytes(final byte[] source, final int sourceOffset, final int length);

  /**
   * Transfers bytes from the given array into the cartridge, beginning at
   * the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge at which the first byte will be written
   * @param source the source array
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@code {@link Cartridge#remaining()} - source.length}
   */
  void setBytes(final long offset, final byte[] source);

  /**
   * Transfers {@code length} bytes from the given array into the cartridge, beginning at
   * the given offset in the cartridge and at the given offset in the array.
   *
   * @param offset the offset in the cartridge at which the first byte will be written
   * @param source the source array
   * @param sourceOffset the offset within the array of the first byte to be read
   * @param length the number of bytes to be written
   * @throws IndexOutOfBoundsException if {@code offset} is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - length)}; or {@code sourceOffset}
   *         is out of bounds, i.e. less than 0 or greater than {@code (source.length - length)}; or
   *         {@link Cartridge#remaining()} is less than {@code length}
   */
  void setBytes(final long offset, final byte[] source, final int sourceOffset, final int length);

  // TODO Add back? I believe this is too niche
  /*
   * Copies the bytes
   * @param sourceOffset
   * @param destOffset
   * @param length
   *
  void interCopy(final long sourceOffset, final long destOffset, final int length);*/

  // TODO Document CharacterCodingException

  /**
   * Decodes {@code length} bytes to characters using the given charset, beginning at
   * the cartridge's current offset, which is then incremented by {@code length}.
   *
   * @param length the number of bytes to be read
   * @param charset the charset to be used
   * @return a string containing the decoded characters
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than {@code length}
   */
  String readString(final int length, final Charset charset);

  /**
   * Decodes {@code length} bytes to characters using the given charset, beginning at
   * the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge from which the first byte will be read
   * @param length the number of bytes to be read
   * @param charset the charset to be used
   * @return a string containing the decoded characters
   * @throws IndexOutOfBoundsException if {@code offset} is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - length)}
   */
  String getString(final long offset, final int length, final Charset charset);

  /**
   * Decodes {@code length} bytes to characters using the {@link StandardCharsets#US_ASCII} charset,
   * beginning at the cartridge's current offset, which is then incremented by {@code length}.
   *
   * @param length the number of bytes to be read
   * @return a string containing the decoded characters
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than {@code length}
   */
  String readAscii(final int length);

  /**
   * Decodes {@code length} bytes to characters using the {@link StandardCharsets#US_ASCII} charset,
   * beginning at the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge from which the first byte will be read
   * @param length the number of bytes to be read
   * @return a string containing the decoded characters
   * @throws IndexOutOfBoundsException if {@code offset} is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - length)}
   */
  String getAscii(final long offset, final int length);

  /**
   * Decodes {@code length} bytes to characters using the {@link StandardCharsets#UTF_8} charset,
   * beginning at the cartridge's current offset, which is then incremented by {@code length}.
   *
   * @param length the number of bytes to be read
   * @return a string containing the decoded characters
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than {@code length}
   */
  String readUtf8(final int length);

  /**
   * Decodes {@code length} bytes to characters using the {@link StandardCharsets#UTF_8} charset,
   * beginning at the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge from which the first byte will be read
   * @param length the number of bytes to be read
   * @return a string containing the decoded characters
   * @throws IndexOutOfBoundsException if {@code offset} is out of bounds, i.e. less than 0 or
   *         greater than {@code ({@link Cartridge#size()} - length)}
   */
  String getUtf8(final long offset, final int length);

  /**
   * Writes the encoded characters using the given charset, beginning at
   * the cartridge's current offset, which is then incremented by the number of
   * written bytes.
   *
   * @param sequence the sequence of characters to be encoded
   * @param charset the charset to be used
   * @return the number of bytes written to the cartridge
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()}} is less than the number
   *         of bytes to be written
   */
  @SuppressWarnings("UnusedReturnValue")
  int writeString(final CharSequence sequence, final Charset charset);

  /**
   * Writes the encoded characters using the given charset, beginning at
   * the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge at which the first byte will be written
   * @param sequence the sequence of characters to be encoded
   * @param charset the charset to be used
   * @return the number of bytes written to the cartridge
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@link Cartridge#size()} minus the number of bytes to be written
   */
  @SuppressWarnings("UnusedReturnValue")
  int setString(final long offset, final CharSequence sequence, final Charset charset);

  /**
   * Writes the encoded characters using the {@link StandardCharsets#US_ASCII} charset,
   * beginning at the cartridge's current offset, which is then incremented by the number of
   * written bytes.
   *
   * @param sequence the sequence of characters to be encoded
   * @return the number of bytes written to the cartridge
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than the number
   *         of bytes to be written
   */
  @SuppressWarnings("UnusedReturnValue")
  int writeAscii(final CharSequence sequence);

  /**
   * Writes the encoded characters using the {@link StandardCharsets#US_ASCII} charset,
   * beginning at the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge at which the first byte will be written
   * @param sequence the sequence of characters to be encoded
   * @return the number of bytes written to the cartridge
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@link Cartridge#size()} minus the number of bytes to be written
   */
  @SuppressWarnings("UnusedReturnValue")
  int setAscii(final long offset, final CharSequence sequence);

  /**
   * Writes the encoded characters using the {@link StandardCharsets#UTF_8} charset,
   * beginning at the cartridge's current offset, which is then incremented by the number of
   * written bytes.
   *
   * @param sequence the sequence of characters to be encoded
   * @return the number of bytes written to the cartridge
   * @throws IndexOutOfBoundsException if {@link Cartridge#remaining()} is less than the number
   *         of bytes to be written
   */
  @SuppressWarnings("UnusedReturnValue")
  int writeUtf8(final CharSequence sequence);

  /**
   * Writes the encoded characters using the {@link StandardCharsets#UTF_8} charset,
   * beginning at the given offset in the cartridge.
   *
   * @param offset the offset in the cartridge at which the first byte will be written
   * @param sequence the sequence of characters to be encoded
   * @return the number of bytes written to the cartridge
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than {@link Cartridge#size()} minus the number of bytes to be written
   */
  @SuppressWarnings("UnusedReturnValue")
  int setUtf8(final long offset, final CharSequence sequence);
}
