package me.hugmanrique.cartage;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test utilities.
 */
public final class TestUtils {

  private static final Path ROMS_DIR = Path.of("roms");

  /**
   * Returns the path to a cartridge file.
   *
   * @param filename the cartridge file name
   * @return the cartridge's path
   * @throws IllegalArgumentException if the file does not exist
   */
  public static Path getCartridge(final String filename) {
    Path path = ROMS_DIR.resolve(filename);
    if (!Files.exists(path)) {
      throw new IllegalArgumentException(path + " does not exist");
    }
    return path;
  }

  private TestUtils() {
    throw new AssertionError();
  }
}
