package me.hugmanrique.cartage.gba;

import static java.util.Objects.requireNonNull;

import java.nio.ByteBuffer;
import me.hugmanrique.cartage.util.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The default {@link GBACartridge.Header} implementation.
 */
final class GBACartridgeHeaderImpl implements GBACartridge.Header {

  private static final int ENTRY_INSTR_ADDR = GBACartridge.Header.ENTRY_INSTRUCTION_ADDR;
  private static final int ENTRY_POINT_ADDR = 0x0;
  private static final int BRANCH_OPCODE = 0b11101010; // B{AL}
  private static final int MAX_BRANCH_OFFSET = (1 << 23) - 1; // words

  private static final int LOGO_ADDR = 0x4;
  private static final int LOGO_LENGTH = GBACartridge.Header.LOGO_LENGTH;
  static final byte[] VALID_LOGO;

  static {
    int[] validLogo = new int[]
      {
        0x24FFAE51, 0x699AA221, 0x3D84820A, 0x84E409AD,
        0x11248B98, 0xC0817F21, 0xA352BE19, 0x9309CE20,
        0x10464A4A, 0xF82731EC, 0x58C7E833, 0x82E3CEBF,
        0x85F4DF94, 0xCE4B09C1, 0x94568AC0, 0x1372A7FC,
        0x9F844D73, 0xA3CA9A61, 0x5897A327, 0xFC039876,
        0x231DC761, 0x0304AE56, 0xBF388400, 0x40A70EFD,
        0xFF52FE03, 0x6F9530F1, 0x97FBC085, 0x60D68025,
        0xA963BE03, 0x014E38E2, 0xF9A234FF, 0xBB3E0344,
        0x780090CB, 0x88113A94, 0x65C07C63, 0x87F03CAF,
        0xD625E48B, 0x380AAC72, 0x21D4F807
      };

    var buffer = ByteBuffer.allocate(LOGO_LENGTH);
    var intBuffer = buffer.asIntBuffer();
    intBuffer.put(validLogo);
    VALID_LOGO = buffer.array();
  }

  private static final int TITLE_ADDR = 0xA0;
  private static final int TITLE_LENGTH = 12;
  private static final int CODE_ADDR = 0xAC; // UTTD
  private static final int CODE_LENGTH = 4;
  private static final int TYPE_ADDR = CODE_ADDR; // U
  private static final int SHORT_TITLE_ADDR = CODE_ADDR + 1; // TT
  private static final int SHORT_TITLE_LENGTH = 2;
  private static final int DEST_ADDR = CODE_ADDR + 3; // D
  private static final int LICENSEE_ADDR = 0xB0;
  private static final int DACS_ADDR = 0xB4;
  private static final int VERSION_ADDR = 0xBC;
  private static final int CHECKSUM_ADDR = 0xBD; // header checksum
  private static final int CHECKSUM_START = 0xA0;
  private static final int CHECKSUM_END = 0xBC;

  private static String prepareString(final String value, final int expectedLength,
                                      final boolean pad) {
    requireNonNull(value);
    StringUtils.requireUppercaseAscii(value);
    if (pad) {
      StringUtils.requireMaxLength(value, expectedLength);
      return StringUtils.padEnd(value, expectedLength, '\0');
    } else {
      StringUtils.requireLength(value, expectedLength);
      return value;
    }
  }

  private final GBACartridge cartridge;

  public GBACartridgeHeaderImpl(final GBACartridge cartridge) {
    this.cartridge = requireNonNull(cartridge);
  }

  /**
   * Returns whether the given ARM7TDMI instruction is a {@code B} instruction.
   * The condition is ignored.
   *
   * @param instr the instruction
   * @return {@code true} if the instruction is a {@code B} instruction
   * @see <a href="https://www.ecs.csun.edu/~smirzaei/docs/ece425/arm7tdmi_instruction_set_reference.pdf">Section 4.5 in the ARM7TDMI Instruction Set Reference</a>
   */
  private static boolean isBranchInstruction(final int instr) {
    return ((instr >> 24) & 0xF) == 0b1010;
  }

  @Override
  public int entryPoint() {
    int instr = this.cartridge.getInt(ENTRY_POINT_ADDR);
    if (!isBranchInstruction(instr)) {
      throw new IllegalStateException("Entry point instruction is not a B instruction");
    }

    // Last 24 bytes of instruction contain the signed address offset.
    // Shift left to discard opcode and set sign bit, and then shift back,
    // converting from words to bytes (8 - 2 = 6).
    int offset = ((instr << 8) >> 6); // bytes
    return ENTRY_INSTR_ADDR + offset;
  }

  @Override
  public void setEntryPoint(final int address) {
    if ((address & 0x3) != 0) {
      throw new IllegalArgumentException("Got non-word-aligned address" + address);
    }
    int offset = (address - ENTRY_INSTR_ADDR) >> 2; // words
    if (Math.abs(offset) > MAX_BRANCH_OFFSET) {
      throw new IllegalArgumentException("Entry point address " + address + " is out of bounds");
    }
    int instr = (BRANCH_OPCODE << 24) | (offset & 0xFFFFFF); // B{AL} offset
    this.cartridge.setInt(ENTRY_POINT_ADDR, instr);
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
    this.cartridge.setAscii(TITLE_ADDR, prepareString(title, TITLE_LENGTH, true));
  }

  @Override
  public String code() {
    return this.cartridge.getAscii(CODE_ADDR, CODE_LENGTH);
  }

  @Override
  public void setCode(final String code) {
    this.cartridge.setAscii(CODE_ADDR, prepareString(code, CODE_LENGTH, false));
  }

  @Override
  public GBACartridge.@Nullable Type type() {
    byte value = this.cartridge.getByte(TYPE_ADDR);
    return GBACartridge.Type.of(value);
  }

  @Override
  public void setType(final GBACartridge.Type type) {
    requireNonNull(type);
    this.cartridge.setByte(TYPE_ADDR, type.value());
  }

  @Override
  public String shortTitle() {
    return this.cartridge.getAscii(SHORT_TITLE_ADDR, SHORT_TITLE_LENGTH);
  }

  @Override
  public void setShortTitle(final String value) {
    this.cartridge.setAscii(SHORT_TITLE_ADDR, prepareString(value, SHORT_TITLE_LENGTH, false));
  }

  @Override
  public GBACartridge.@Nullable Destination destination() {
    byte value = this.cartridge.getByte(DEST_ADDR);
    return GBACartridge.Destination.of(value);
  }

  @Override
  public void setDestination(final GBACartridge.Destination destination) {
    requireNonNull(destination);
    this.cartridge.setByte(DEST_ADDR, destination.value());
  }

  @Override
  public short licensee() {
    return this.cartridge.getShort(LICENSEE_ADDR);
  }

  @Override
  public void setLicensee(final short licensee) {
    this.cartridge.setShort(LICENSEE_ADDR, licensee);
  }

  @Override
  public GBACartridge.@Nullable DACSType dacs() {
    byte value = this.cartridge.getByte(DACS_ADDR);
    return GBACartridge.DACSType.of(value);
  }

  @Override
  public void setDacs(final GBACartridge.DACSType type) {
    requireNonNull(type);
    this.cartridge.setByte(DACS_ADDR, type.value());
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
      checksum -= this.cartridge.getByte(offset);
    }
    return (byte) (checksum - 0x19);
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
}
