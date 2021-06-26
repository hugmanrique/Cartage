/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Resources and data used for testing.
 */
public final class TestResources {

  private static final Path ROMS_DIR = Path.of("roms");
  private static final byte[] PRIMES;

  static {
    try {
      PRIMES = getResourceBytes("primes.txt");
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

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

  /**
   * Finds and returns a resource with the given name.
   *
   * @param name the resource name
   * @return the read bytes
   * @throws IOException if an I/O error occurs
   */
  public static byte[] getResourceBytes(final String name) throws IOException {
    try (final InputStream stream = TestResources.class.getClassLoader()
        .getResourceAsStream(name)) {
      Objects.requireNonNull(stream, "Cannot find \"" + name + '"');
      return stream.readAllBytes();
    }
  }

  /**
   * Returns a copy of the contents of the {@code primes.txt} resource.
   *
   * @return a copy of the {@link #PRIMES} array
   */
  public static byte[] getPrimes() {
    return PRIMES.clone();
  }

  private TestResources() {
    throw new AssertionError();
  }
}
