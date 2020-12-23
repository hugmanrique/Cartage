package me.hugmanrique.cartage.gba;

import java.nio.ByteOrder;
import me.hugmanrique.cartage.AbstractCartridge;

/**
 * The default {@link GBACartridge} implementation.
 */
final class GBACartridgeImpl extends AbstractCartridge implements GBACartridge {

  private final GBACartridge.Header header;

  public GBACartridgeImpl(final byte[] data) {
    super(data, ByteOrder.LITTLE_ENDIAN);
    this.header = new GBACartridgeHeaderImpl(this);
  }

  @Override
  public Header header() {
    return this.header;
  }
}
