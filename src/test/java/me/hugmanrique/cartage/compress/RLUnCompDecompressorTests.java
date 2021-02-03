package me.hugmanrique.cartage.compress;

import me.hugmanrique.cartage.compression.RLUnCompDecompressor;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link RLUnCompDecompressor}.
 */
public class RLUnCompDecompressorTests {

  private static final RLUnCompDecompressor DECOMPRESSOR = RLUnCompDecompressor.get();

  @Test
  void testInvalidHeader() {
    /*var cartridge = SimpleCartridge.from(new byte[3], ByteOrder.LITTLE_ENDIAN);
    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge, 2));*/
  }

  @Test
  void testEmpty() {

  }

  @Test
  void testLiteralRun() {

  }

  @Test
  void testReplicatedRun() {

  }

  @Test
  void testMix() {

  }

  @Test
  void testInvalid() {

  }
}
