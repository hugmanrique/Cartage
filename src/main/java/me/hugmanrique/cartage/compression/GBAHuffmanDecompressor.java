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

      final int dataSize = (header >> 28) & DATA_SIZE; // bits
      final int length = header & DECOMPRESSED_LENGTH;

      return new byte[0];
    } catch (final IndexOutOfBoundsException e) {
      throw new DecompressionException("Got corrupted Huffman-compressed data");
    }
  }
}
