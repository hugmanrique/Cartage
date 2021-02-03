package me.hugmanrique.cartage.gba;

import static java.util.Objects.requireNonNull;

import java.nio.ByteOrder;
import jdk.incubator.foreign.MemorySegment;
import me.hugmanrique.cartage.AbstractCartridge;

/**
 * The default {@link GBACartridge} implementation.
 */
final class GBACartridgeImpl extends AbstractCartridge implements GBACartridge {

  private final GBACartridge.Header header;

  GBACartridgeImpl(final byte[] data) {
    super(MemorySegment.ofArray(requireNonNull(data)), ByteOrder.LITTLE_ENDIAN);
    this.header = new GBACartridgeHeaderImpl(this);
  }

  @Override
  public Header header() {
    return this.header;
  }
}
