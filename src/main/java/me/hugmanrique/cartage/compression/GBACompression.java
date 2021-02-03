package me.hugmanrique.cartage.compression;

/**
 * Compression and decompression utilities for GBA and Nintendo DS cartridges.
 *
 * @see <a href="https://problemkaputt.de/gbatek.htm#biosdecompressionfunctions">GBATEK</a>
 */
public final class GBACompression {

  // Magic numbers
  static final byte LZ77 = 0x10;
  static final byte RL = 0x30;
  static final byte HUFFMAN = 0x20;

  static void checkCompressionType(final int header, final byte expectedType, final String typeName)
      throws DecompressionException {
    final int actual = (header >>> 24) & 0xF;
    if (actual != expectedType) {
      throw new DecompressionException("Expected " + expectedType + " type for "
          + typeName + "-compressed data, got " + actual + " instead");
    }
  }

  private GBACompression() {
    throw new AssertionError();
  }
}
