/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import static me.hugmanrique.cartage.compression.GBACompression.checkCompressionType;

import me.hugmanrique.cartage.Cartridge;
import me.hugmanrique.cartage.util.NumberUtils;

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

  private static final byte TYPE = 2;
  private static final int BIT_DEPTH = 24;
  private static final int DECOMPRESSED_LENGTH = 0xFFFFFF;
  private static final int CHILD_IS_LEAF = 0x80;
  private static final int CHILD_OFFSET = 0x3F;
  private static final long ALIGN_BASE_OFFSET = ~0x1;

  private GBAHuffmanDecompressor() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      checkCompressionType(header, TYPE, "HF");

      // The compressed data contains a binary tree and a set of paths starting at
      // the root node of the tree, encoded as a sequence of 32-bit integers.
      // The bits of the integer indicate whether to follow the left node (0) or
      // the right node (1), starting at the most-significant bit. A path ends
      // when a leaf node is reached, which contains an uncompressed value of
      // bitDepth bits (usually 4 or 8).

      final int bitDepth = (header >>> BIT_DEPTH) & 0xF;
      if (!NumberUtils.isPowerOf2(bitDepth)) {
        throw new DecompressionException("Bit depth must be a power of 2, got " + bitDepth);
      }

      final int length = header & DECOMPRESSED_LENGTH;
      final byte[] result = new byte[length];

      final int treeLength = (cartridge.readByte() + 1) << 1;
      final long rootNodeOffset = cartridge.offset();
      final long pathsOffset = rootNodeOffset + treeLength;

      int index = 0;
      int currentCount = 0; // number of bits written to result[index]
      while (index < length) {
        final int paths = cartridge.getInt(pathsOffset + index);

        for (byte i = 31; i >= 0; i--) {
          final int direction = (paths >>> i) & 0x1; // left or right child
          final byte node = cartridge.readByte();

          final int nextDelta = (((node & CHILD_OFFSET) + 1) << 1) | direction;
          final long nextOffset = (cartridge.offset() & ALIGN_BASE_OFFSET) + nextDelta;
          cartridge.setOffset(nextOffset);

          // If the 7th (6th) bit is set, the left (right) child is a leaf
          final boolean nextIsLeaf = ((node << direction) & CHILD_IS_LEAF) != 0;
          if (nextIsLeaf) {
            final byte value = cartridge.readByte();
            result[index] = (byte) ((result[index] << bitDepth) | value);
            currentCount += bitDepth;
            if (currentCount == 8) {
              index++;
            }

            cartridge.setOffset(rootNodeOffset);
          }
        }
      }

      cartridge.setOffset(pathsOffset + length); // end of sequence of paths
      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted Huffman-compressed data", e);
    }
  }
}
