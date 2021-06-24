/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import static me.hugmanrique.cartage.compression.GBACompression.requireTypeNibble;

import me.hugmanrique.cartage.Cartridge;

/**
 * Implements the DiffUnFilter algorithms with 8-bit and 16-bit data sizes, present in the BIOS of
 * the GBA and Nintendo DS.
 *
 * @see <a href="https://problemkaputt.de/gbatek.htm#biosdecompressionfunctions">GBATEK</a>
 */
public final class GBADiffUnfilterer implements Decompressor {

  private static final GBADiffUnfilterer INSTANCE = new GBADiffUnfilterer();

  /**
   * Returns an unfilterer instance.
   *
   * @return the unfilterer
   */
  public static GBADiffUnfilterer get() {
    return INSTANCE;
  }

  private static final byte TYPE = 8;
  private static final int DATA_SIZE = 0xF;
  private static final int DECOMPRESSED_LENGTH = 8;
  private static final int BYTE_DELTAS = 1;
  private static final int SHORT_DELTAS = 2;

  private GBADiffUnfilterer() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      requireTypeNibble(header, TYPE, "UnFilter");

      int dataSize = header & DATA_SIZE;
      final int length = header >>> DECOMPRESSED_LENGTH;
      final byte[] result = new byte[length];

      switch (dataSize) {
        case BYTE_DELTAS -> decompressByteDeltas(cartridge, result, length);
        case SHORT_DELTAS -> decompressShortDeltas(cartridge, result, length);
        default -> throw new DecompressionException("Invalid data size " + dataSize + ", expected "
            + BYTE_DELTAS + " or " + SHORT_DELTAS);
      }
      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted DiffUnfilter data", e);
    }
  }

  /**
   * Applies the Diff8bitUnFilterWrite8bit algorithm, reading {@code length} deltas from the given
   * cartridge and writing the absolute values to the destination array.
   *
   * @param source the cartridge whose contents are to be unfiltered
   * @param dest the destination array
   * @param length the number of bytes to read
   */
  private void decompressByteDeltas(final Cartridge source, final byte[] dest, final int length) {
    byte absolute = 0;
    for (int i = 0; i < length; i++) {
      final byte delta = source.readByte();
      absolute += delta;
      dest[i] = absolute;
    }
  }

  /**
   * Applies the Diff16bitUnFilter algorithm, reading {@code length / 2} deltas from the given
   * cartridge and writing the absolute values to the destination array.
   *
   * @param source the cartridge whose contents are to be unfiltered
   * @param dest the destination array
   * @param length the number of bytes to read
   */
  private void decompressShortDeltas(final Cartridge source, final byte[] dest, final int length) {
    short absolute = 0;
    for (int i = 0; i < length; ) {
      final short delta = source.readShort();
      absolute += delta;
      dest[i++] = (byte) (absolute >>> 8);
      dest[i++] = (byte) absolute;
    }
  }
}
