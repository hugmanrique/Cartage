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
public final class GBALZSSDecompressor implements Decompressor {

  private static final GBALZSSDecompressor INSTANCE = new GBALZSSDecompressor();

  /**
   * Returns a decompressor instance.
   *
   * @return the decompressor
   */
  public static GBALZSSDecompressor get() {
    return INSTANCE;
  }

  private static final byte MAGIC_NUMBER = 0x10;
  private static final int DECOMPRESSED_LENGTH = 0xFFFFFF;
  private static final int BLOCK_COUNT = 8;
  private static final int COMPRESSED = 0x80;
  private static final int DISP_BASELINE = 1;
  private static final int DISP_MSB = 0x0F;

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      checkCompressionType(header, MAGIC_NUMBER, "LZSS");

      final int length = header & DECOMPRESSED_LENGTH;
      final byte[] result = new byte[length];
      int index = 0;

      while (index < length) {
        // The compressed data is divided in groups of 8 blocks. A flag byte indicates the type of
        // each block, where a 0 bit specifies the block data byte should be copied verbatim; and
        // a 1 bit indicates the block is compressed, in which case the 2 data bytes contain
        // a displacement and the number of bytes to copy (minus 3) within the result array.
        byte flags = cartridge.readByte();
        for (int i = 0; i < BLOCK_COUNT; i++) {
          boolean compressed = (flags & COMPRESSED) != 0;
          if (compressed) {
            // Copy count bytes starting at offset (index - displacement) of result into
            // result starting at offset index. The length and displacement values are
            // contained in the next 2 bytes. Their layout is a bit unnatural.
            // Some Pokémon Ruby/Sapphire tilesets have an invalid count value, i.e.
            // greater than the remaining number of bytes; set this upper bound.
            final int meta = cartridge.readByte();
            final int count = Math.min((meta >>> 4) + 3, length - index);
            final int displacement = DISP_BASELINE
                + (((meta & DISP_MSB) << 8) // the most-significant 4 bits
                    | cartridge.readByte()); // the least-significant 8 bits

            final int srcPos = index - displacement;
            if (srcPos < 0) {
              throw new DecompressionException("Invalid displacement " + displacement
                  + " at index " + index);
            }
            System.arraycopy(result, srcPos, result, index, count);
            index += count;
          } else {
            result[index++] = cartridge.readByte();
          }
          flags <<= 1;
        }
      }
      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted LZSS-compressed data", e);
    }
  }

  private GBALZSSDecompressor() {}
}
