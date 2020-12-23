package me.hugmanrique.cartage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import me.hugmanrique.cartage.gb.GBCartridge;

/**
 * Represents the contents of a cartridge as a byte buffer.
 *
 * <p>Changing properties does not update the relevant checksums. This must be done manually.
 *
 * @see GBCartridge for accessing Game Boy cartridges
 */
public interface Cartridge extends BufferAccessor {

  /**
   * Writes the cartridge contents to the given path.
   *
   * @param path the path
   * @throws IOException if an I/O error occurs
   */
  void writeTo(final Path path) throws IOException;

  /**
   * Writes the cartridge contents to the given stream. The stream is not closed.
   *
   * @param stream the output stream
   * @throws IOException if an I/O error occurs
   */
  void writeTo(final OutputStream stream) throws IOException;
}
