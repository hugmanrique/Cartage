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
import java.util.Arrays;
import me.hugmanrique.cartage.compression.DecompressionException;
import me.hugmanrique.cartage.compression.GBADiffUnfilterer;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link GBADiffUnfilterer}.
 */
public class GBADiffUnfiltererTests {

  private static final GBADiffUnfilterer UNFILTERER = GBADiffUnfilterer.get();

  @Test
  void testInvalidTypeThrows() {
    final var cartridge = fromData(new byte[4], ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> UNFILTERER.decompress(cartridge));
  }

  @Test
  void testInvalidDataSizeThrows() {
    final var header = new byte[] { (byte) 0x80, 0, 0, 0 }; // data size = 0
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> UNFILTERER.decompress(cartridge));
  }

  @Test
  void testEmpty8Bit() {
    final var header = new byte[] { (byte) 0x81, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);
    final byte[] result = UNFILTERER.decompress(cartridge);

    assertEquals(0, result.length);
  }

  @Test
  void testEmpty16Bit() {
    final var header = new byte[] { (byte) 0x82, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.LITTLE_ENDIAN);
    final byte[] result = UNFILTERER.decompress(cartridge);

    assertEquals(0, result.length);
  }

  @Test
  void testPrimes8BitDeltas() throws IOException {
    final var cartridge =  fromData(
        getResourceBytes("primes_diffunfilter8"), ByteOrder.LITTLE_ENDIAN);
    final byte[] result = UNFILTERER.decompress(cartridge);

    assertArrayEquals(getPrimes(), result);
  }

  @Test
  void testPrimes16BitDeltas() throws IOException {
    final var cartridge = fromData(
        getResourceBytes("primes_diffunfilter16"), ByteOrder.LITTLE_ENDIAN);
    // The cartridge has an odd size, and the unfilterer expects a 2 byte-aligned length.
    // The compressed file was truncated, removing the trailing newline.
    final byte[] result = Arrays.copyOfRange(UNFILTERER.decompress(cartridge), 0, 7177);

    assertArrayEquals(getPrimes(), result);
  }
}
