package me.hugmanrique.cartage.compress;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.ByteOrder;
import me.hugmanrique.cartage.SimpleCartridge;
import me.hugmanrique.cartage.compression.DecompressionException;
import me.hugmanrique.cartage.compression.RLUnCompDecompressor;
import org.junit.jupiter.api.Test;

public class RLUnCompDecompressorTests {

  private static final RLUnCompDecompressor DECOMPRESSOR = RLUnCompDecompressor.get();

  @Test
  void testInvalidHeader() {
    var cartridge = SimpleCartridge.from(new byte[3], ByteOrder.LITTLE_ENDIAN);
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
