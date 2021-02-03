package me.hugmanrique.cartage.compression;

import me.hugmanrique.cartage.Cartridge;

/**
 * Implements the LZ77UnCompRead, present in the BIOS of the GBA and Nintendo DS.
 *
 * @see <a href="https://problemkaputt.de/gbatek.htm#biosdecompressionfunctions">GBATEK</a>
 */
public final class GBALZ77Decompressor implements Decompressor {

  private static final GBALZ77Decompressor INSTANCE = new GBALZ77Decompressor();

  /**
   * Returns a decompressor instance.
   *
   * @return the decompressor
   */
  public static GBALZ77Decompressor get() {
    return INSTANCE;
  }

  private GBALZ77Decompressor() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    return new byte[0];
  }
}
