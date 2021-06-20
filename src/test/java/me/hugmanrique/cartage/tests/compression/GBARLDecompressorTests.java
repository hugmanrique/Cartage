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
import me.hugmanrique.cartage.compression.GBARLDecompressor;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link GBARLDecompressor}.
 */
public class GBARLDecompressorTests {

  private static final GBARLDecompressor DECOMPRESSOR = GBARLDecompressor.get();

  @Test
  void testInvalidHeader() {
    var cartridge = fromData(new byte[3], ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge, 2));
  }

  @Test
  void testEmpty() {
    var header = new byte[] { 0x30, 0, 0, 0 };
    var cartridge = fromData(header, ByteOrder.BIG_ENDIAN);
    byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertEquals(0, result.length);
    assertEquals(4, cartridge.offset(), "offset is incremented");
  }

  @Test
  void testEmptyFromOffset() {
    final var header = new byte[] { 0x12, 0x34, 0, 0, 0, 0x30 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);
    cartridge.setOffset(5);
    final byte[] result = DECOMPRESSOR.decompress(cartridge, 2);

    assertEquals(0, result.length);
    assertEquals(5, cartridge.offset(), "offset is preserved");
  }

  @Test
  void testLiteralRun() {
    final var compressed = new byte[] {
        0x30, 0, 0, 5, // header
        5, (byte) 0xF0, 0x0, (byte) 0xBA, 0x12, 0x34 // literal run of 5 bytes
      };
    final var cartridge = fromData(compressed, ByteOrder.BIG_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertEquals(5, result.length);
    assertEquals((byte) 0xF0, result[0]);
    assertEquals(0, result[1]);
    assertEquals((byte) 0xBA, result[2]);
    assertEquals(0x12, result[3]);
    assertEquals(0x34, result[4]);
    assertEquals(compressed.length, cartridge.offset(), "offset is modified");
  }

  @Test
  void testLiteralRunTooShortThrows() {
    final var compressed = new byte[] {
        0x30, 0, 0, 4,
        4, 1, 2, 3 // only 3 bytes, expected 4
      };

    assertThrows(DecompressionException.class, () ->
        DECOMPRESSOR.decompress(fromData(compressed, ByteOrder.BIG_ENDIAN)));
  }

  @Test
  void testReplicatedRun() {

  }

  @Test
  void testMix() {

  }
}
