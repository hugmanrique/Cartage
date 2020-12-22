package me.hugmanrique.cartage.gb;

import static me.hugmanrique.cartage.gb.GBCartridgeHeaderImpl.GLOBAL_CHECKSUM_ADDR;

import me.hugmanrique.cartage.AbstractCartridge;

/**
 * The default {@link GBCartridge} implementation.
 */
final class GBCartridgeImpl extends AbstractCartridge implements GBCartridge {

  private final GBCartridge.Header header;

  GBCartridgeImpl(final byte[] data) {
    super(data);
    this.header = new GBCartridgeHeaderImpl(this);
  }

  @Override
  public Header header() {
    return this.header;
  }

  @Override
  public short computeChecksum() {
    final int length = this.header.romSizeBytes();
    short checksum = 0;
    for (int i = 0; i < length; i++) {
      if (i == GLOBAL_CHECKSUM_ADDR || i == GLOBAL_CHECKSUM_ADDR + 1) {
        continue; // skip checksum bytes
      }
      checksum += this.getByte(i);
    }
    return checksum;
  }
}
