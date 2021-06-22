/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests.compression;

import static me.hugmanrique.cartage.tests.DummyCartridge.fromData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.ByteOrder;
import me.hugmanrique.cartage.compression.DecompressionException;
import me.hugmanrique.cartage.compression.GBAHuffmanDecompressor;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link GBAHuffmanDecompressor}.
 */
public class GBAHuffmanDecompresorTests {

  private static final GBAHuffmanDecompressor DECOMPRESSOR = GBAHuffmanDecompressor.get();

  @Test
  void testInvalidType() {
    final var cartridge = fromData(new byte[7], ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
  }

  @Test
  void testBitDepthMustBePowerOf2() {
    final var header = new byte[] { 0x23, 0, 0, 0, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.BIG_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
  }

  @Test
  void testEmpty() {
    final var header = new byte[] { 0x20, 0, 0, 0, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.BIG_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertEquals(0, result.length);
    assertEquals(7, cartridge.offset(), "offset is incremented");
  }
}
