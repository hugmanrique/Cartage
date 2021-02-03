package me.hugmanrique.cartage.compress;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.ByteOrder;
import me.hugmanrique.cartage.DummyCartridge;
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
    var cartridge = new DummyCartridge(new byte[3], ByteOrder.LITTLE_ENDIAN);
    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge));
    assertThrows(DecompressionException.class, () -> DECOMPRESSOR.decompress(cartridge, 2));
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
