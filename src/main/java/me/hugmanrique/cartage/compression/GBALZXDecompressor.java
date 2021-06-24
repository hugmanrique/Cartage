/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import static me.hugmanrique.cartage.compression.GBACompression.requireTypeByte;

import me.hugmanrique.cartage.Cartridge;
import me.hugmanrique.cartage.util.BufferUtils;

/**
 * Implements the LZX decompression algorithm in big-endian mode, used in some Nintendo DS games.
 *
 * @see <a href="https://gbatemp.net/threads/nintendo-ds-gba-compressors.313278/">CUE Nintendo
 *     DS/GBA compressors</a>
 */
public final class GBALZXDecompressor implements Decompressor {

  private static final GBALZXDecompressor INSTANCE = new GBALZXDecompressor();

  /**
   * Returns a decompressor instance.
   *
   * @return the decompressor
   */
  public static GBALZXDecompressor get() {
    return INSTANCE;
  }

  private static final byte TYPE = 0x11;
  private static final int DECOMPRESSED_LENGTH = 8;
  private static final int BLOCK_COUNT = 8;
  private static final int COMPRESSED = 0x80;
  private static final int DATA_TYPE = 12;
  private static final int COUNT_BASELINE_0 = 0x11;
  private static final int COUNT_BASELINE_1 = 0x111;
  private static final int DISP_BASELINE = 1;

  private GBALZXDecompressor() {}

  @Override
  @SuppressWarnings("NarrowingCompoundAssignment") // flags <<= 1 is harmless
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      requireTypeByte(header, TYPE, "LZX");

      final int length = header >>> DECOMPRESSED_LENGTH;
      final byte[] result = new byte[length];
      int index = 0;

      while (index < length) {
        // The compressed data is divided in groups of 8 blocks. A flag byte indicates the type of
        // each block, where a 0 bit specifies the block data byte should be copied verbatim; and
        // a 1 bit indicates the block is compressed, in which case the data bytes contain
        // a displacement and the number of bytes to copy within the result array.
        byte flags = cartridge.readByte();
        for (int i = 0; i < BLOCK_COUNT && index < length; i++, flags <<= 1) {
          boolean compressed = (flags & COMPRESSED) != 0;
          if (compressed) {
            // Copy count bytes starting at offset (index - displacement) of result into
            // result starting at offset index. The type value determines the number of
            // bytes the count and displacement parameters occupy, as well as the base
            // amount of bytes to copy.
            int data = (cartridge.readByte() << 8) | cartridge.readUnsignedByte();
            final int type = data >>> DATA_TYPE;
            int baseCount;
            switch (type) {
              case 0 -> {
                // 8-bit count followed by 12-bit displacement
                data = ((data & 0xFFF) << 8) | cartridge.readUnsignedByte();
                baseCount = COUNT_BASELINE_0;
              }
              case 1 -> {
                // 16-bit count followed by 12-bit displacement
                data = ((data & 0xFFF) << 16)
                    | (cartridge.readUnsignedByte() << 8)
                    | cartridge.readUnsignedByte();
                baseCount = COUNT_BASELINE_1;
              }
              default -> { // type >= 2 forms part of the count
                // 4-bit count followed by 12-bit displacement
                baseCount = 1;
              }
            }

            final int count = (data >>> 12) + baseCount;
            final int displacement = (data & 0xFFF) + DISP_BASELINE;
            final int srcPos = index - displacement;
            if (srcPos < 0) {
              throw new DecompressionException("Invalid displacement " + displacement
                  + " at offset " + cartridge.offset());
            }

            BufferUtils.copyByteByByte(result, srcPos, index, count);
            index += count;
          } else {
            result[index++] = cartridge.readByte();
          }
        }
      }
      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted LZX-compressed data", e);
    }
  }
}
