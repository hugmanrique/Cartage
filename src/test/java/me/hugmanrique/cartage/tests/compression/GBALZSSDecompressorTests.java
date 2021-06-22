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
import me.hugmanrique.cartage.compression.GBALZSSDecompressor;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link GBALZSSDecompressor}.
 */
public class GBALZSSDecompressorTests {

  private static final GBALZSSDecompressor DECOMPRESSOR = GBALZSSDecompressor.get();

  @Test
  void testInvalidType() {
    final var cartridge = fromData(new byte[3], ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge, 2));
  }

  @Test
  void testEmpty() {
    final var header = new byte[] { 0x10, 0x0, 0x0, 0x0 };
    final var cartridge = fromData(header, ByteOrder.BIG_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertEquals(0, result.length);
    assertEquals(4, cartridge.offset(), "offset is incremented");
  }

  @Test
  void testEmptyFromOffset() {
    final var header = new byte[] { 0x12, 0x34, 0, 0, 0, 0x10 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);
    cartridge.setOffset(5);
    final byte[] result = DECOMPRESSOR.decompress(cartridge, 2);

    assertEquals(0, result.length);
    assertEquals(5, cartridge.offset(), "offset is preserved");
  }
}
