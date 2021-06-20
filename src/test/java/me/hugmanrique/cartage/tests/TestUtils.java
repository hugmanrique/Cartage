/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests;

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
