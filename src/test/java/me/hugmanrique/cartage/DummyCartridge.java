package me.hugmanrique.cartage;

import java.nio.ByteOrder;
import jdk.incubator.foreign.MemorySegment;

/**
 * A {@link Cartridge} with no special functionality.
 */
public class DummyCartridge extends AbstractCartridge {

  /**
   * Constructs a {@link DummyCartridge}.
   *
   * @param data the cartridge data
   * @param order the cartridge's byte order
   */
  public DummyCartridge(final byte[] data, final ByteOrder order) {
    super(MemorySegment.ofArray(data), order);
  }
}
