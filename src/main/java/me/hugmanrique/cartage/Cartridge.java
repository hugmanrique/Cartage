package me.hugmanrique.cartage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.file.Path;
import me.hugmanrique.cartage.gb.GBCartridge;
import me.hugmanrique.cartage.gba.GBACartridge;

// TODO Tweak javadoc
// TODO Document thread confinement, likely to change on future Panama versions
/**
 * Represents the contents of a cartridge as a byte buffer.
 *
 * <p>The cartridge has a certain size, and an offset position for accessing data.
 * The size is non-negative and immutable. The offset is the zero-based index of
 * the next element to be read or written.
 *
 * <p>Cartridges are closed explicitly (see {@link #close()}). When a cartridge is closed,
 * the underlying resources associated with said cartridge might be deallocated, and subsequent
 * operation on the cartridge will fail with {@link IllegalStateException}.
 *
 * @see GBCartridge for accessing Game Boy cartridges
 * @see GBACartridge for accessing Game Boy Advance cartridges
 */
public interface Cartridge extends CartridgeAccessors, AutoCloseable {

  /**
   * Returns the cartridge's byte order.
   *
   * @return the current byte order
   * @throws IllegalStateException if the cartridge is closed
   */
  ByteOrder order();

  /**
   * Sets the cartridge's byte order.
   *
   * @param order the new byte order
   * @throws IllegalStateException if the cartridge is closed
   */
  void order(final ByteOrder order);

  /**
   * Returns the cartridge's offset.
   *
   * @return the current offset, in bytes
   * @throws IllegalStateException if the cartridge is closed
   */
  long offset();

  /**
   * Sets the cartridge's offset.
   *
   * @param offset the new offset, in bytes
   * @throws IndexOutOfBoundsException if the given offset is out of bounds, i.e. less than 0 or
   *         greater than or equal to {@link #size()}
   * @throws IllegalStateException if the cartridge is closed
   */
  void setOffset(final long offset);

  /**
   * Returns the size of the cartridge.
   *
   * @return the cartridge size, in bytes
   * @throws IllegalStateException if the cartridge is closed
   */
  long size(); // TODO document immutable

  /**
   * Returns the number of bytes between the current offset and the end of the cartridge.
   *
   * @return the number of remaining bytes
   * @throws IllegalStateException if the cartridge is closed
   */
  long remaining();

  /**
   * Returns whether there exist any bytes between the current offset and
   * the end of the cartridge.
   *
   * @return {@code true} if and only if {@link #remaining()} equals 0
   * @throws IllegalStateException if the cartridge is closed
   */
  boolean hasRemaining();

  /**
   * Closes the cartridge. Once closed, any subsequent operation on the cartridge will fail with
   * {@link IllegalStateException}.
   *
   * @throws IllegalStateException if the cartridge is closed
   */
  @Override
  void close();

  /**
   * Writes the cartridge contents to the given path.
   *
   * @param path the path
   * @throws IOException if an I/O error occurs
   * @throws IllegalStateException if the cartridge is closed
   */
  void writeTo(final Path path) throws IOException;

  // TODO Remove array copy in AbstractCartridge to lift maximum size restriction
  /**
   * Writes the cartridge contents to the given stream. The stream is not closed.
   *
   * @param stream the output stream
   * @throws UnsupportedOperationException if this cartridge's contents cannot be copied into
   *         a {@link byte[]} instance, e.g. its size is greater than {@link Integer#MAX_VALUE}
   * @throws IOException if an I/O error occurs
   * @throws IllegalStateException if the cartridge is closed
   */
  void writeTo(final OutputStream stream) throws IOException;
}
