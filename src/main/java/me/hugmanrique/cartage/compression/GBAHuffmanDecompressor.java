/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import static me.hugmanrique.cartage.compression.GBACompression.requireTypeNibble;

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

  private static final byte TYPE = 0x2;
  private static final int BIT_DEPTH = 0xF;
  private static final int DECOMPRESSED_LENGTH = 8;
  private static final int CHILD_IS_LEAF = 0x80;
  private static final int CHILD_OFFSET = 0x3F;
  private static final long ALIGN_BASE_OFFSET = ~0x1;

  private GBAHuffmanDecompressor() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      requireTypeNibble(header, TYPE, "HF");

      // The compressed data contains a binary tree and a set of paths starting at
      // the root node of the tree, encoded as a sequence of 32-bit integers.
      // The bits of the integer indicate whether to follow the left node (0) or
      // the right node (1), starting at the most-significant bit. A path ends
      // when a leaf node is reached, which contains an uncompressed value of
      // bitDepth bits (usually 4 or 8).
      final byte bitDepth = (byte) (header & BIT_DEPTH);
      if (bitDepth == 0 || !NumberUtils.isPowerOf2(bitDepth)) {
        throw new DecompressionException(
            "Bit depth must be a positive power of 2, got " + bitDepth);
      }
      final int length = header >>> DECOMPRESSED_LENGTH;
      final byte[] result = new byte[length];

      final int treeLength = (cartridge.readUnsignedByte() + 1) << 1;
      final long rootNodeOffset = cartridge.offset();
      long nodeOffset = rootNodeOffset;
      cartridge.skip(treeLength - 1); // start of paths

      int index = 0;
      byte bitCount = 0; // number of bits written to result[index]
      while (index < length) {
        final int paths = cartridge.readInt();
        for (byte i = 31; i >= 0; i--) {
          final int direction = (paths >>> i) & 0x1; // left or right child
          final byte node = cartridge.getByte(nodeOffset);

          final int nextDelta = (((node & CHILD_OFFSET) + 1) << 1) | direction;
          nodeOffset = (nodeOffset & ALIGN_BASE_OFFSET) + nextDelta;

          // If the 7th (6th) bit is set, the left (right) child is a leaf
          final boolean nextIsLeaf = ((node << direction) & CHILD_IS_LEAF) != 0;
          if (nextIsLeaf) {
            final byte value = cartridge.getByte(nodeOffset);
            result[index] = (byte) ((result[index] << bitDepth) | value);
            bitCount += bitDepth;
            if (bitCount == 8) {
              bitCount = 0;
              if (++index == length) {
                break; // we're done, remaining paths are padding
              }
            }
            nodeOffset = rootNodeOffset;
          }
        }
      }
      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted Huffman-compressed data", e);
    }
  }
}
