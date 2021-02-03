package me.hugmanrique.cartage.gb;

import static java.util.Objects.requireNonNull;

import me.hugmanrique.cartage.util.StringUtils;

/**
 * The default {@link GBCartridge.Header} implementation.
 */
final class GBCartridgeHeaderImpl implements GBCartridge.Header {

  private static final int ENTRY_POINT_ADDR = 0x100;
  private static final int LOGO_ADDR = 0x104;
  static final byte[] VALID_LOGO = new byte[]
    {
      (byte) 0xCE, (byte) 0xED, 0x66, 0x66, (byte) 0xCC, 0x0D, 0x00, 0x0B, 0x03, 0x73, 0x00,
      (byte) 0x83, 0x00, 0x0C, 0x00, 0x0D, 0x00, 0x08, 0x11, 0x1F, (byte) 0x88, (byte) 0x89,
      0x00, 0x0E, (byte) 0xDC, (byte) 0xCC, 0x6E, (byte) 0xE6, (byte) 0xDD, (byte) 0xDD,
      (byte) 0xD9, (byte) 0x99, (byte) 0xBB, (byte) 0xBB, 0x67, 0x63, 0x6E, 0x0E, (byte) 0xEC,
      (byte) 0xCC, (byte) 0xDD, (byte) 0xDC, (byte) 0x99, (byte) 0x9F, (byte) 0xBB, (byte) 0xB9,
      0x33, 0x3E
    };

  private static final int TITLE_ADDR = 0x134;
  private static final int TITLE_LENGTH = 16;
  private static final int MANUFACTURER_ADDR = 0x13F;
  private static final int MANUFACTURER_LENGTH = 4;

  private static final int GBC_FLAG_ADDR = 0x143;
  private static final byte SUPPORTS_GBC = (byte) 0x80;
  private static final byte REQUIRES_GBC = (byte) 0xC0;
  private static final int SGB_FLAG_ADDR = 0x146;
  private static final byte SUPPORTS_SGB = 0x3;

  private static final int OLD_LICENSEE_ADDR = 0x14B;
  private static final byte USE_NEW_LICENSEE = 0x33;
  private static final int NEW_LICENSEE_ADDR = 0x144;
  private static final short NEW_LICENSEE_NONE = 0;

  private static final int TYPE_ADDR = 0x147;
  private static final int ROM_SIZE_ADDR = 0x148;
  private static final int BASE_ROM_SIZE = 1 << 15; // 32 KB
  private static final int RAM_SIZE_ADDR = 0x149;
  private static final int DEST_CODE_ADDR = 0x14A;
  private static final int VERSION_ADDR = 0x14C;
  private static final int CHECKSUM_ADDR = 0x14D; // header checksum
  private static final int CHECKSUM_START = 0x134;
  private static final int CHECKSUM_END = 0x14C;
  static final int GLOBAL_CHECKSUM_ADDR = 0x14E; // whole ROM

  private static void checkHeaderString(final String value, final int expectedLength) {
    requireNonNull(value);
    StringUtils.requireLength(value, expectedLength);
    StringUtils.requireUppercaseAscii(value);
  }

  private final GBCartridge cartridge;

  GBCartridgeHeaderImpl(final GBCartridge cartridge) {
    this.cartridge = requireNonNull(cartridge);
  }

  /**
   * Returns whether the given Z80 operation code represents a {@code JP} or
   * {@code CALL} instruction.
   *
   * @param opcode the Z80 opcode
   * @return {@code true} if {@code opcode} represents a {@code JP} or {@code CALL} instruction
   * @see <a href="http://www.zilog.com/docs/z80/um0080.pdf">Table 15 in the Z80 User Manual</a>
   */
  private static boolean isJumpOrCallOpcode(final int opcode) {
    return switch (opcode) {
      // This is missing the parity and sign conditions. No cartridge uses these.
      case 0xC3, 0xD8, 0xD2, 0xCA, 0xC2, 0xCD, 0xDC, 0xD4, 0xCC, 0xC4 -> true;
      default -> false;
    };
  }

  @Override
  public short entryPoint() {
    // The entry point is 4 bytes and must contain a JP or CALL instruction (which take 3 bytes).
    // Thus, the opcode of this instruction is at byte 0 or 1.
    int offset = ENTRY_POINT_ADDR;
    do {
      int opcode = this.cartridge.getUnsignedByte(offset);
      if (isJumpOrCallOpcode(opcode)) {
        // Read destination address
        return this.cartridge.getShort(offset + 1);
      }
    } while (offset++ <= ENTRY_POINT_ADDR + 1);

    throw new IllegalStateException("Entry point contains no JP or CALL instruction");
  }

  @Override
  public void setEntryPoint(final short entryPoint) {
    this.cartridge.setByte(ENTRY_POINT_ADDR, (byte) 0x0); // NOP
    this.cartridge.setByte(ENTRY_POINT_ADDR + 1, (byte) 0xC3); // unconditional JP
    this.cartridge.setShort(ENTRY_POINT_ADDR + 2, entryPoint); // dest
  }

  @Override
  public byte[] logo() {
    byte[] bitmap = new byte[LOGO_LENGTH];
    this.logo(bitmap);
    return bitmap;
  }

  @Override
  public void logo(final byte[] dest) {
    if (dest.length != LOGO_LENGTH) {
      throw new IllegalArgumentException("Invalid dest array length " + dest.length);
    }
    this.cartridge.getBytes(LOGO_ADDR, dest);
  }

  @Override
  public void setLogo(final byte[] source) {
    if (source.length != LOGO_LENGTH) {
      throw new IllegalArgumentException("Invalid source array length " + source.length);
    }
    this.cartridge.setBytes(LOGO_ADDR, source);
  }

  @Override
  public void setValidLogo() {
    this.setLogo(VALID_LOGO);
  }

  @Override
  public String title() {
    return this.cartridge.getAscii(TITLE_ADDR, TITLE_LENGTH);
  }

  @Override
  public void setTitle(final String title) {
    checkHeaderString(title, TITLE_LENGTH);
    this.cartridge.setAscii(TITLE_ADDR, title);
  }

  @Override
  public String manufacturer() {
    return this.cartridge.getAscii(MANUFACTURER_ADDR, MANUFACTURER_LENGTH);
  }

  @Override
  public void setManufacturer(final String manufacturer) {
    checkHeaderString(manufacturer, MANUFACTURER_LENGTH);
    this.cartridge.setAscii(MANUFACTURER_ADDR, manufacturer);
  }

  @Override
  public byte gbc() {
    return this.cartridge.getByte(GBC_FLAG_ADDR);
  }

  @Override
  public void setGbc(final byte value) {
    this.cartridge.setByte(GBC_FLAG_ADDR, value);
  }

  @Override
  public boolean hasColorFunctions() {
    byte gbc = this.gbc();
    return gbc == SUPPORTS_GBC || gbc == REQUIRES_GBC;
  }

  @Override
  public boolean requiresColor() {
    return this.gbc() == REQUIRES_GBC;
  }

  @Override
  public short licensee() {
    byte oldCode = this.cartridge.getByte(OLD_LICENSEE_ADDR);
    if (oldCode == USE_NEW_LICENSEE) {
      return this.cartridge.getShort(NEW_LICENSEE_ADDR);
    }
    return oldCode;
  }

  @Override
  public void setOldLicensee(final byte licensee) {
    this.cartridge.setByte(OLD_LICENSEE_ADDR, licensee);
    this.cartridge.setShort(NEW_LICENSEE_ADDR, NEW_LICENSEE_NONE); // clear new licensee
  }

  @Override
  public void setNewLicensee(final short licensee) {
    this.cartridge.setByte(OLD_LICENSEE_ADDR, USE_NEW_LICENSEE);
    this.cartridge.setShort(NEW_LICENSEE_ADDR, licensee);
  }

  @Override
  public byte sgb() {
    return this.cartridge.getByte(SGB_FLAG_ADDR);
  }

  @Override
  public void setSgb(final byte value) {
    this.cartridge.setByte(SGB_FLAG_ADDR, value);
  }

  @Override
  public boolean hasSuperFunctions() {
    return this.sgb() == SUPPORTS_SGB;
  }

  @Override
  public GBCartridge.Type type() {
    byte value = this.cartridge.getByte(TYPE_ADDR);
    return GBCartridge.Type.of(value);
  }

  @Override
  public void setType(final GBCartridge.Type type) {
    requireNonNull(type);
    this.cartridge.setByte(TYPE_ADDR, type.value());
  }

  @Override
  public byte romSize() {
    return this.cartridge.getByte(ROM_SIZE_ADDR);
  }

  @Override
  public int romSizeBytes() {
    byte code = this.romSize();
    return switch (code) {
      case 0, 1, 2, 3, 4, 5, 6, 7, 8 -> BASE_ROM_SIZE << code;
      case 0x52 -> 0x120000;
      case 0x53 -> 0x140000;
      case 0x54 -> 0x180000;
      default -> throw new IllegalArgumentException("Invalid ROM size code " + code);
    };
  }

  @Override
  public void setRomSize(final byte code) {
    if ((code < 0 || code > 8) && (code < 0x52 || code > 0x54)) {
      throw new IllegalArgumentException("Invalid ROM size code " + code);
    }
    this.cartridge.setByte(ROM_SIZE_ADDR, code);
  }

  @Override
  public byte ramSize() {
    return this.cartridge.getByte(RAM_SIZE_ADDR);
  }

  @Override
  public int ramSizeBytes() {
    byte code = this.ramSize();
    return switch (code) {
      case 0 -> 0;
      case 1 -> 1 << 11; // 2 KB
      case 2 -> 1 << 13; // 8 KB
      case 3 -> 1 << 15; // 32 KB
      case 4 -> 1 << 17; // 128 KB
      case 5 -> 1 << 16; // 64 KB
      default -> throw new IllegalStateException("Invalid RAM size code " + code);
    };
  }

  @Override
  public void setRamSize(final byte code) {
    if (code < 0 || code > 5) {
      throw new IllegalArgumentException("Invalid RAM size code " + code);
    }
    this.cartridge.setByte(RAM_SIZE_ADDR, code);
  }

  @Override
  public boolean destination() {
    return this.cartridge.getByte(DEST_CODE_ADDR) == 1;
  }

  @Override
  public boolean japaneseDistribution() {
    return !this.destination(); // 0 => Japanese, 1 => Global
  }

  @Override
  public void setDestination(final boolean destination) {
    byte code = destination ? (byte) 1 : 0;
    this.cartridge.setByte(DEST_CODE_ADDR, code);
  }

  @Override
  public byte version() {
    return this.cartridge.getByte(VERSION_ADDR);
  }

  @Override
  public void setVersion(final byte version) {
    this.cartridge.setByte(VERSION_ADDR, version);
  }

  @Override
  public byte checksum() {
    return this.cartridge.getByte(CHECKSUM_ADDR);
  }

  @Override
  public byte computeChecksum() {
    byte checksum = 0;
    for (int offset = CHECKSUM_START; offset <= CHECKSUM_END; offset++) {
      checksum -= (byte) (this.cartridge.getByte(offset) + 1);
    }
    return checksum;
  }

  @Override
  public void setChecksum(final byte checksum) {
    this.cartridge.setByte(CHECKSUM_ADDR, checksum);
  }

  @Override
  public byte setChecksum() {
    byte checksum = this.computeChecksum();
    this.setChecksum(checksum);
    return checksum;
  }

  // Global checksum is big endian

  @Override
  public short globalChecksum() {
    return Short.reverseBytes(this.cartridge.getShort(GLOBAL_CHECKSUM_ADDR));
  }

  @Override
  public void setGlobalChecksum(short checksum) {
    checksum = Short.reverseBytes(checksum);
    this.cartridge.setShort(GLOBAL_CHECKSUM_ADDR, checksum);
  }
}
