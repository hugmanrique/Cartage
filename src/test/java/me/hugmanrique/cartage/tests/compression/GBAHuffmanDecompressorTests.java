/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests.compression;

import static me.hugmanrique.cartage.tests.DummyCartridge.fromData;
import static me.hugmanrique.cartage.tests.TestResources.getPrimes;
import static me.hugmanrique.cartage.tests.TestResources.getResourceBytes;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteOrder;
import me.hugmanrique.cartage.compression.DecompressionException;
import me.hugmanrique.cartage.compression.GBAHuffmanDecompressor;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link GBAHuffmanDecompressor}.
 */
public class GBAHuffmanDecompressorTests {

  private static final GBAHuffmanDecompressor DECOMPRESSOR = GBAHuffmanDecompressor.get();

  @Test
  void testInvalidTypeThrows() {
    final var cartridge = fromData(new byte[7], ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
  }

  @Test
  void testNonPowerOf2BitDepthThrows() {
    final var header = new byte[] { 0x23, 0, 0, 0, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
  }

  @Test
  void testEmpty() {
    // tree length = 2 => start of paths = 6
    final var header = new byte[] { 0x24, 0, 0, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertEquals(0, result.length);
    assertEquals(6, cartridge.offset(), "offset is incremented");
  }

  @Test
  void testPrimes4BitDepth() throws IOException {
    final var cartridge = fromData(
        getResourceBytes("primes_huffman4"), ByteOrder.LITTLE_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertArrayEquals(getPrimes(), result);
  }

  @Test
  void testPrimes8BitDepth() throws IOException {
    final var cartridge = fromData(
        getResourceBytes("primes_huffman8"), ByteOrder.LITTLE_ENDIAN);
    final byte[] result = DECOMPRESSOR.decompress(cartridge);

    assertArrayEquals(getPrimes(), result);
  }
}
