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
 * Implements the RLUnCompRead algorithm, present in the BIOS of the GBA and Nintendo DS.
 *
 * @see <a href="https://problemkaputt.de/gbatek.htm#biosdecompressionfunctions">GBATEK</a>
 */
public final class GBARLDecompressor implements Decompressor {

  private static final GBARLDecompressor INSTANCE = new GBARLDecompressor();

  /**
   * Returns a decompressor instance.
   *
   * @return the decompressor
   */
  public static GBARLDecompressor get() {
    return INSTANCE;
  }

  private static final byte MAGIC_NUMBER = 0x30;
  private static final int DECOMPRESSED_LENGTH = 0xFFFFFF;
  private static final byte REPLICATE_RUN = (byte) 0x80;
  private static final byte RUN_LENGTH = ~REPLICATE_RUN;

  private GBARLDecompressor() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      checkCompressionType(header, MAGIC_NUMBER, "RL");

      final int length = header & DECOMPRESSED_LENGTH;
      final byte[] result = new byte[length];
      int index = 0;

      while (index < length) {
        // The compressed data is made out of variable-length runs. The sign bit of the first byte
        // of each run specifies whether the run contents should be interpreted verbatim (0), or
        // a given byte should be repeated a number of times (1). The remaining bits specify
        // the length of the run.
        final byte flag = cartridge.readByte();
        final boolean replicate = (flag & REPLICATE_RUN) != 0;
        final int runLength = flag & RUN_LENGTH;

        if (replicate) {
          byte value = cartridge.readByte();
          // TODO Replace by array copy, segments don't provide this yet.
          for (int i = 0; i < runLength + 2; i++) { // 2 repetition baseline
            result[index++] = value;
          }
        } else {
          for (int i = 0; i < runLength; i++) {
            result[index++] = cartridge.readByte();
          }
        }
      }
      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted RLE-compressed data", e);
    }
  }
}
