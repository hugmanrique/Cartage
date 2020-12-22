package me.hugmanrique.cartage.gb;

import static java.util.Objects.requireNonNull;

import me.hugmanrique.cartage.util.StringUtils;

/**
 * The default {@link GBCartridge.Header} implementation.
 */
final class GBCartridgeHeaderImpl implements GBCartridge.Header {

  private static final int ENTRY_POINT_ADDR = 0x100;
  private static final int LOGO_ADDR = 0x104;
  //private static final int LOGO_LENGTH = 48;
  static final byte[] VALID_LOGO = new byte[] {
    (byte) 0xCE, (byte) 0xED, 0x66, 0x66, (byte) 0xCC, 0x0D, 0x00, 0x0B, 0x03, 0x73, 0x00,
    (byte) 0x83, 0x00, 0x0C, 0x00, 0x0D, 0x00, 0x08, 0x11, 0x1F, (byte) 0x88, (byte) 0x89,
    0x00, 0x0E, (byte) 0xDC, (byte) 0xCC, 0x6E, (byte) 0xE6, (byte) 0xDD, (byte) 0xDD,
    (byte)0xD9, (byte)0x99, (byte) 0xBB, (byte) 0xBB, 0x67, 0x63, 0x6E, 0x0E, (byte) 0xEC,
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
  private static final int CHECKSUM_START_ADDR = 0x134;
  private static final int CHECKSUM_END_ADDR = 0x14C;
  static final int GLOBAL_CHECKSUM_ADDR = 0x14F; // whole ROM

  private static void checkHeaderString(final String value, final int expectedLength) {
    requireNonNull(value);
    StringUtils.requireLength(value, expectedLength);
    if (!StringUtils.isUpperCase(value)) {
      throw new IllegalArgumentException("Expected all uppercase string, got " + value);
    }
    if (!StringUtils.isAscii(value)) {
      throw new IllegalArgumentException(value + " contains non-ASCII characters");
    }
  }

  private final GBCartridge cartridge;

  GBCartridgeHeaderImpl(final GBCartridge cartridge) {
    this.cartridge = requireNonNull(cartridge);
  }

  @Override
  public int entryPoint() {
    return this.cartridge.getInt(ENTRY_POINT_ADDR);
  }

  @Override
  public void setEntryPoint(final int entryPoint) {
    this.cartridge.setInt(ENTRY_POINT_ADDR, entryPoint);
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
      throw new IllegalArgumentException("Invalid logo array length " + dest.length);
    }
    this.cartridge.getBytes(LOGO_ADDR, dest);
  }

  @Override
  public void setLogo(final byte[] data) {
    if (data.length != LOGO_LENGTH) {
      throw new IllegalArgumentException("Invalid logo array length " + data.length);
    }
    this.cartridge.setBytes(LOGO_ADDR, data);
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
    return BASE_ROM_SIZE << this.romSize();
  }

  @Override
  public void setRomSize(final byte code) {
    if (code < 0 || code > 8) {
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
    for (int offset = CHECKSUM_START_ADDR; offset <= CHECKSUM_END_ADDR; offset++) {
      checksum -= this.cartridge.getByte(offset) + 1;
    }
    return checksum;
  }

  @Override
  public void setChecksum(final byte checksum) {
    this.cartridge.setByte(CHECKSUM_ADDR, checksum);
  }

  @Override
  public short globalChecksum() {
    return this.cartridge.getShort(GLOBAL_CHECKSUM_ADDR);
  }

  @Override
  public void setGlobalChecksum(final short checksum) {
    this.cartridge.setShort(GLOBAL_CHECKSUM_ADDR, checksum);
  }
}
