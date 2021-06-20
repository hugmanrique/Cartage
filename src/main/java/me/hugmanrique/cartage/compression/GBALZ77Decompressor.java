/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import static me.hugmanrique.cartage.compression.GBACompression.checkCompressionType;

import me.hugmanrique.cartage.Cartridge;

/**
 * Implements the LZ77UnCompRead algorithm, present in the BIOS of the GBA and Nintendo DS.
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

  private static final int DECOMPRESSED_LENGTH = 0xFFFFFF;
  private static final int BLOCK_COUNT = 8;
  private static final int DISP_MSB = 0x0F;

  private GBALZ77Decompressor() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      checkCompressionType(header, GBACompression.LZ77, "LZ77");

      final int length = header & DECOMPRESSED_LENGTH;
      final byte[] result = new byte[length];
      int index = 0;

      while (index < length) {
        // The compressed data is divided in groups of 8 blocks. A flag byte indicates the type of
        // each block, where a 0 bit specifies the block data byte should be copied verbatim; and
        // a 1 bit indicates the block is compressed, in which case the 2 data bytes contain
        // a displacement and the number of bytes to copy (minus 3) within the result array.
        final byte flags = cartridge.readByte();
        for (int i = 0; i < BLOCK_COUNT; i++) {
          boolean compressed = (flags & (0x80 >> i)) != 0;
          if (compressed) {
            // Copy (count + 3) bytes starting at offset (length - displacement - 1) of result
            // into result at offset index through (index + count + 3). The length and displacement
            // values are contained in the next 2 bytes. Their layout is a bit unnatural.
            int meta = cartridge.readByte();
            final int count = (meta >> 4) + 3;
            int displacement = (meta & DISP_MSB) << 8;
            displacement |= cartridge.readByte(); // the LS 8 bits

            if (displacement >= length) {
              throw new DecompressionException("Invalid displacement " + displacement
                  + " for uncompressed length " + length);
            }
            System.arraycopy(result, length - displacement - 1, result, index, count);
            index += count;
          } else {
            result[index++] = cartridge.readByte();
          }
        }
      }
      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted LZ77-compressed data", e);
    }
  }
}
