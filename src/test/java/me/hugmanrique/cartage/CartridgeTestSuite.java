package me.hugmanrique.cartage;

import static java.util.Objects.requireNonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

/**
 * Tests the behavior of a {@link Cartridge} type.
 *
 * @param <T> the cartridge type
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CartridgeTestSuite<T extends Cartridge> {

  protected final T cartridge;
  private final byte[] original;

  protected CartridgeTestSuite(final T cartridge) {
    this.cartridge = requireNonNull(cartridge);
    try {
      // To restore original contents, we write into an output stream and then grab its contents.
      var stream = new ByteArrayOutputStream();
      cartridge.writeTo(stream);
      this.original = stream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Cannot write original cartridge contents", e);
    }
  }

  protected void reset() {
    // Slow, but most generic way without resorting to suppliers
    this.cartridge.setBytes(0, original);
  }

  @BeforeEach
  void beforeAll() {
    this.reset();
  }

  @AfterAll
  void afterAll() {
    this.cartridge.close();
  }
}
