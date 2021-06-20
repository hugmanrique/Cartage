/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import static java.util.Objects.requireNonNull;

import me.hugmanrique.cartage.Cartridge;

/**
 * Decompresses data from a {@link Cartridge}.
 *
 * <p>Unless otherwise noted, implementations are thread-safe.
 */
public interface Decompressor {

  // TODO Most decompressors throw IndexOutOfBoundsException when reading from cartridges
  //  and catching them incurs some performance loss. Deprecate DecompressionException?

  /**
   * Decompresses data starting at the given offset from the given cartridge. The offset of the
   * cartridge is not modified.
   *
   * @param cartridge the cartridge
   * @param offset the offset where compressed data starts
   * @return the decompressed data
   * @throws DecompressionException if an error occurs while attempting decompression
   * @throws IllegalArgumentException if the given offset is out of bounds, i.e. less than 0 or
   *     greater than or equal to {@linkplain Cartridge#size() {@code cartridge.size()}}
   */
  default byte[] decompress(final Cartridge cartridge, final long offset)
      throws DecompressionException {
    requireNonNull(cartridge);
    final long prevOffset = cartridge.offset();
    cartridge.setOffset(offset); // does bounds check
    try {
      return this.decompress(cartridge);
    } finally {
      cartridge.setOffset(prevOffset);
    }
  }

  byte[] decompress(final Cartridge cartridge) throws DecompressionException;
}
