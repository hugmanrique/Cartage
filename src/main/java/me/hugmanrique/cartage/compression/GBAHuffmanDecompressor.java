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

  private static final byte MAGIC_NUMBER = 0x20;
  private static final int DATA_SIZE = 0xF;
  private static final int DECOMPRESSED_LENGTH = 0xFFFFFF;

  private GBAHuffmanDecompressor() {}

  @Override
  public byte[] decompress(final Cartridge cartridge) throws DecompressionException {
    try {
      final int header = cartridge.readInt();
      checkCompressionType(header, MAGIC_NUMBER, "HF");

      final int bitDepth = (header >> 28) & DATA_SIZE;
      final int length = header & DECOMPRESSED_LENGTH;
      final int treeSize = (cartridge.readByte() + 1) * 2;
      final byte[] result = new byte[length];

      final long rootNodeOffset = cartridge.offset();
      long nodeOffset = rootNodeOffset;
      cartridge.skip(treeSize);

      // The compressed data contains a binary tree and a set of paths starting at
      // the root node of the tree, encoded as a sequence of 32-bit integers.
      // The bits of the integer indicate whether to follow the left node (0) or
      // the right node (1), starting at the most-significant bit. A path ends
      // when a leaf node is reached, which contains an uncompressed symbol.
      // This traversal process is repeated with the remaining bits of the integer.
      for (int index = 0; index < length; index++) {
        final int slice = cartridge.getInt(treeSize + index);

        for (int i = 31; i >= 0; i--) {
          final int goLeft = (slice >>> i) & 0x1;
          final byte nodeData = cartridge.getByte(nodeOffset);
          final boolean nextIsLeaf = ((nodeData << goLeft) & 0x80) != 0;
          //nodeOffset =

          if (nextIsLeaf) {
          }
        }


        /*for (int i = 0; i < 32; i++, slice <<= 1) {
          final boolean left = (slice & 31) != 0;
          final byte treeData = cartridge.readByte();
          //final boolean nextIsLeaf = treeData & ()
        }*/
      }

      /*int index = 0;

      while (index < length) {
        final int path = cartridge.readInt();
        for (int i = 0; i < Integer.BYTES * 8; i++) {

        }


        //final int value = cartridge.readInt();

      }*/




      return result;
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted Huffman-compressed data");
    }
  }
}
