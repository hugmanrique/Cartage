package me.hugmanrique.cartage.compression;

import me.hugmanrique.cartage.Cartridge;

/**
 * Decompresses data from a {@link Cartridge}.
 *
 * <p>Unless otherwise noted, implementations are thread-safe.
 */
public interface Decompressor {

  /**
   * Decompresses data starting at the given offset from the given cartridge.
   * The offset of the cartridge is not modified.
   *
   * @param cartridge the cartridge
   * @param offset the offset where compressed data starts
   * @return the decompressed data
   * @throws DecompressionException if an error occurs while attempting decompression
   */
  byte[] decompress(final Cartridge cartridge, final int offset) throws DecompressionException;

  default byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    return this.decompress(cartridge, cartridge.offset());
  }
}
