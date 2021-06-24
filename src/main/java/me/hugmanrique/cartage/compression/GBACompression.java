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
final class GBACompression {

  /**
   * Checks that the first 8 bits of the given header equal {@code expected}.
   *
   * @param header the header value
   * @param expected the expected type value
   * @param name the type name
   * @throws DecompressionException if the type value does not equal {@code expected}
   * @see #requireTypeNibble(int, byte, String) to only validate the second nibble
   */
  static void requireTypeByte(final int header, final byte expected, final String name) {
    final int actual = header & 0xFF;
    if (actual != expected) {
      throw new DecompressionException("Expected " + expected + " type byte for "
          + name + "-compressed data, got " + actual);
    }
  }

  /**
   * Checks that the bits in range [4, 7] of the given header equal {@code expected}.
   *
   * @param header the header value
   * @param expected the expected type value
   * @param name the type name
   * @throws DecompressionException if the type value does not equal {@code expected}
   * @see #requireTypeByte(int, byte, String) to validate the first 8 bits
   */
  static void requireTypeNibble(final int header, final byte expected, final String name) {
    final int actual = (header >>> 4) & 0xF;
    if (actual != expected) {
      throw new DecompressionException("Expected " + expected + " type nibble for "
          + name + "-compressed data, got " + actual);
    }
  }

  private GBACompression() {
    throw new AssertionError();
  }
}
