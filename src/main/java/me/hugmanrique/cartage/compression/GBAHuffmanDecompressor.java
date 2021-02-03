package me.hugmanrique.cartage.compression;

import me.hugmanrique.cartage.Cartridge;

/**
 * Implements the HuffUnCompRead algorithm, present in the BIOS of the GBA and Nintendo DS.
 *
 * @see <a href="https://problemkaputt.de/gbatek.htm#biosdecompressionfunctions">GBATEK</a>
 */
public final class GBAHuffmanDecompressor implements Decompressor {

  private static final GBAHuffmanDecompressor INSTANCE = new GBAHuffmanDecompressor();

  /**
   * Returns a decompressor instance.
   *
   * @return the decompressor
   */
  public static GBAHuffmanDecompressor get() {
    return INSTANCE;
  }

  private GBAHuffmanDecompressor() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      return new byte[0];
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted Huffman-compressed data");
    }
  }
}
