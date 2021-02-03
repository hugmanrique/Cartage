package me.hugmanrique.cartage.compression;

import me.hugmanrique.cartage.Cartridge;

/**
 * Implements the RLUnComp algorithm, used by the BIOS of the GBA and Nintendo DS.
 *
 * @see <a href="https://problemkaputt.de/gbatek.htm#biosdecompressionfunctions">GBATEK</a>
 */
public final class RLUnCompDecompressor implements Decompressor {

  private static final RLUnCompDecompressor INSTANCE = new RLUnCompDecompressor();

  /**
   * Returns a decompressor instance.
   *
   * @return the decompressor
   */
  public static RLUnCompDecompressor get() {
    return INSTANCE;
  }

  private static final byte MAGIC_NUMBER = 0x30;
  private static final byte REPLICATED = (byte) 0x80;
  private static final byte DROP_TYPE = (byte) 0x7F;

  private RLUnCompDecompressor() {}

  // TODO error checking
  @Override
  public byte[] decompress(final Cartridge cartridge, final long start) {
    // TODO Check cartridge.remaining >= 4
    long offset = start;
    final int header = cartridge.getInt(offset);
    offset += 4;
    if ((header >>> 24) != MAGIC_NUMBER) {
      throw new DecompressionException("Cannot decompress non-RLE-encoded data");
    }

    final int length = header & 0xFFFFFF;
    final byte[] result = new byte[length];
    if (length == 0) {
      return result;
    }
    final long end = start + length;
    byte runLength;

    do {
      byte flag = cartridge.getByte(offset++);
      runLength = (byte) (flag & DROP_TYPE);
      boolean replicate = (flag & REPLICATED) == REPLICATED;

      if (replicate) {
        runLength = (byte) (runLength + 2); // 2 byte threshold
        byte value = cartridge.getByte(offset++);
        for (int i = 0; i < runLength; i++) {
          result[i] = value;
        }
      } else {
        for (int i = 0; i < runLength; i++) {
          result[i] = cartridge.getByte(offset + i);
        }
      }
    } while ((offset += runLength) < end);

    return result;
  }
}
