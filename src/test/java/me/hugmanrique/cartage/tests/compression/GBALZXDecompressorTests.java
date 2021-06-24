/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests.compression;

import static me.hugmanrique.cartage.tests.DummyCartridge.fromData;
import static me.hugmanrique.cartage.tests.TestUtils.getPrimes;
import static me.hugmanrique.cartage.tests.TestUtils.getResourceBytes;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteOrder;
import me.hugmanrique.cartage.compression.DecompressionException;
import me.hugmanrique.cartage.compression.GBALZXDecompressor;
import me.hugmanrique.cartage.tests.TestUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link GBALZXDecompressor}.
 */
public class GBALZXDecompressorTests {

  private static final GBALZXDecompressor DECOMPRESSOR = GBALZXDecompressor.get();

  @Test
  void testInvalidTypeThrows() {
    final var cartridge = fromData(new byte[3], ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
  }

  @Test
  void testRegularLZThrows() {
    final var header = new byte[] { 0x10, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
  }

  @Test
  void testEmpty() {
    final var header = new byte[] { 0x11, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertEquals(0, result.length);
    assertEquals(4, cartridge.offset(), "offset is incremented");
  }

  @Test
  void testEmptyFromOffset() {
    final var data = new byte[] { 0x25, 0x34, 0x11, 0, 0, 0 };
    final var cartridge = fromData(data, ByteOrder.LITTLE_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge, 2);

    assertEquals(0, result.length);
    assertEquals(0, cartridge.offset(), "offset is preserved");
  }

  @Test
  void testPrimes() throws IOException {
    final var cartridge = fromData(
        getResourceBytes("primes_lzx"), ByteOrder.LITTLE_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertArrayEquals(getPrimes(), result);
  }
}
