/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests.compression;

import static me.hugmanrique.cartage.tests.DummyCartridge.fromData;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import jdk.incubator.foreign.MemorySegment;
import me.hugmanrique.cartage.compression.DecompressionException;
import me.hugmanrique.cartage.compression.GBADiffUnfilterer;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link GBADiffUnfilterer}.
 */
public class GBADiffUnfiltererTests {

  private static final GBADiffUnfilterer UNFILTERER = GBADiffUnfilterer.get();

  @Test
  void testInvalidType() {
    final var cartridge = fromData(new byte[4], ByteOrder.LITTLE_ENDIAN);

    assertThrows(DecompressionException.class, () -> UNFILTERER.decompress(cartridge));
  }

  @Test
  void testInvalidDataSize() {
    final var header = new byte[] { (byte) 0x80, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.BIG_ENDIAN);

    assertThrows(DecompressionException.class, () -> UNFILTERER.decompress(cartridge));
  }

  @Test
  void testEmpty8Bit() {
    final var header = new byte[] { (byte) 0x81, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.BIG_ENDIAN);
    final byte[] result = UNFILTERER.decompress(cartridge);

    assertEquals(0, result.length);
  }

  @Test
  void testEmpty16Bit() {
    final var header = new byte[] { (byte) 0x82, 0, 0, 0 };
    final var cartridge = fromData(header, ByteOrder.BIG_ENDIAN);
    final byte[] result = UNFILTERER.decompress(cartridge);

    assertEquals(0, result.length);
  }

  @Test
  void test8Bit() {
    final var data = new byte[] { (byte) 0x81, 0, 0, 8, 22, 1, 1, 2, -2, -24, 10, -15 };
    final var cartridge = fromData(data, ByteOrder.BIG_ENDIAN);
    final byte[] result = UNFILTERER.decompress(cartridge);

    assertArrayEquals(new byte[] { 22, 23, 24, 26, 24, 0, 10, -5 }, result);
  }

  @Test
  void test16Bit() {
    final ByteBuffer srcBuffer = ByteBuffer.allocate(4 + 9 * 2).order(ByteOrder.BIG_ENDIAN);
    srcBuffer.asShortBuffer()
        .put(new short[] { (short) 0x8200, 9 * 2, 125, 1, 2, -3, -135, -5, 15, 2, -2 });
    final var data = srcBuffer.array();
    final var cartridge = fromData(MemorySegment.ofArray(data), ByteOrder.BIG_ENDIAN);
    final byte[] result = UNFILTERER.decompress(cartridge);

    final ByteBuffer buffer = ByteBuffer.allocate(9 * 2);
    final byte[] expected = buffer.array();
    buffer.asShortBuffer().put(new short[] { 125, 126, 128, 125, -10, -15, 0, 2, 0 });

    assertArrayEquals(expected, result);
  }
}
