package me.hugmanrique.cartage;

import java.nio.ByteOrder;
import me.hugmanrique.cartage.gb.GBCartridge;
import me.hugmanrique.cartage.gba.GBACartridge;

// TODO Tweak javadoc
// TODO Document closing explicitly is required
/**
 * Represents the contents of a cartridge as a byte buffer.
 *
 * <p>The cartridge has a certain size, and an offset position for accessing data.
 * The size is non-negative and immutable. The offset is the zero-based index of
 * the next element to be read or written.
 *
 * @see GBCartridge for accessing Game Boy cartridges
 * @see GBACartridge for accessing Game Boy Advance cartridges
 */
public interface Cartridge extends CartridgeAccessors, AutoCloseable {

  /**
   * Returns the cartridge's byte order.
   *
   * @return the current byte order
   */
  ByteOrder order();

  /**
   * Sets the cartridge's byte order.
   *
   * @param order the new byte order
   */
  void order(final ByteOrder order);

  /**
   * Returns the cartridge's offset.
   *
   * @return the current offset, in bytes
   */
  long offset();

  /**
   * Sets the cartridge's offset.
   *
   * @param offset the new offset, in bytes
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than or equal to {@link #size()}
   */
  void setOffset(final long offset);

  /**
   * Returns the size of the cartridge.
   *
   * @return the cartridge size, in bytes
   */
  long size(); // TODO document immutable

  /**
   * Returns the number of bytes between the current offset and the end of the cartridge.
   *
   * @return the number of remaining bytes
   */
  long remaining();

  /**
   * Returns whether there exist any bytes between the current offset and
   * the end of the cartridge.
   *
   * @return {@code true} if and only if {@link #remaining()} equals 0
   */
  boolean hasRemaining();

  /**
   * Closes the cartridge. Once a cartridge has been closed, any attempt to access its contents
   * will fail with {@link IllegalStateException}.
   */
  @Override
  void close();

  // TODO Make static utility methods?
  /*
   * Writes the cartridge contents to the given path.
   *
   * @param path the path
   * @throws IOException if an I/O error occurs
   *
  void writeTo(final Path path) throws IOException;

  /*
   * Writes the cartridge contents to the given stream. The stream is not closed.
   *
   * @param stream the output stream
   * @throws IOException if an I/O error occurs
   *
  void writeTo(final OutputStream stream) throws IOException;*/
}
