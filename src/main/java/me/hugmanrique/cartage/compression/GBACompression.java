/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

/**
 * Compression and decompression utilities for GBA and Nintendo DS cartridges.
 *
 * @see <a href="https://problemkaputt.de/gbatek.htm#biosdecompressionfunctions">GBATEK</a>
 */
public final class GBACompression {

  static void checkCompressionType(final int header, final byte expectedType, final String typeName)
      throws DecompressionException {
    final int actual = header >>> 28;
    if (actual != expectedType) {
      throw new DecompressionException("Expected " + expectedType + " type for "
          + typeName + "-compressed data, got " + actual + " instead");
    }
  }

  private GBACompression() {
    throw new AssertionError();
  }
}
