package me.hugmanrique.cartage.gb;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import me.hugmanrique.cartage.Cartridge;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A Game Boy ROM cartridge.
 *
 * @see <a href="https://raw.githubusercontent.com/AntonioND/giibiiadvance/master/docs/TCAGBD.pdf">The Cycle-Accurate Game Boy Docs</a>
 * @see <a href="https://gbdev.io/pandocs/#the-cartridge-header">Pan Docs</a>
 */
public interface GBCartridge extends Cartridge {

  /**
   * Reads a cartridge from the given path.
   *
   * @param path the path
   * @return the read cartridge
   * @throws IOException if an I/O error occurs
   */
  static GBCartridge read(final Path path) throws IOException {
    requireNonNull(path);
    byte[] data = Files.readAllBytes(path);
    return new GBCartridgeImpl(data);
  }

  /**
   * Reads a cartridge from the given stream. The stream is not closed.
   *
   * @param stream the input stream
   * @return the read cartridge
   * @throws IOException if an I/O error occurs
   */
  static GBCartridge read(final InputStream stream) throws IOException {
    requireNonNull(stream);
    byte[] data = stream.readAllBytes();
    return new GBCartridgeImpl(data);
  }

  /**
   * Returns the cartridge header.
   *
   * @return the header
   */
  Header header();

  /**
   * Computes the checksum of the ROM contents.
   *
   * @return the computed global checksum
   * @see Header#globalChecksum() to get the global checksum
   * @see Header#setGlobalChecksum(short) to update the global checksum
   */
  short computeChecksum();

  /**
   * Specifies the memory bank controller (if any) and other hardware present in the cartridge.
   */
  enum Type {
    ROM_ONLY(0x0),
    MBC1(0x1),
    MBC1_RAM(0x2),
    MBC1_RAM_BATTERY(0x3),
    // 0x4 unused
    MBC2(0x5),
    MBC2_RAM_BATTERY(0x6),
    ROM_RAM(0x8),
    ROM_RAM_BATTERY(0x9),
    // 0xA unused
    MMM01(0xB),
    MMM01_RAM(0xC),
    MMM01_RAM_BATTERY(0xD),
    // 0xE unused
    MBC3_TIMER_BATTERY(0xF),
    MBC3_RAM_TIMER_BATTERY(0x10),
    MBC3(0x11),
    MBC3_RAM(0x12),
    MBC3_RAM_BATTERY(0x13),
    // 0x14-18 unused
    MBC5(0x19),
    MBC5_RAM(0x1A),
    MBC5_RAM_BATTERY(0x1B),
    MBC5_RUMBLE(0x1C),
    MBC5_RAM_RUMBLE(0x1D),
    MBC5_RAM_BATTERY_RUMBLE(0x1E),
    // 0x1F unused
    MBC6_RAM_BATTERY(0x20),
    // 0x21 unused
    MBC7_RAM_BATTERY_ACCELEROMETER(0x22),
    // 0x23-FB unused
    POCKET_CAMERA(0xFC),
    @SuppressWarnings("SpellCheckingInspection")
    BANDAI_TAMA5(0xFD),
    HUC3(0xFE),
    HUC1_RAM_BATTERY(0xFF);

    /**
     * Returns the cartridge type identified by the given value.
     *
     * @param value the type identifier
     * @return the type with the given identifier, or {@code null} if not found
     */
    public static @Nullable Type of(final byte value) {
      for (var type : values()) {
        if (type.value() == value) {
          return type;
        }
      }
      return null;
    }

    private final byte value;

    Type(final int value) {
      this.value = (byte) value;
    }

    /**
     * Returns the identifier for this cartridge type.
     *
     * @return the type identifier
     */
    public byte value() {
      return value;
    }

    /**
     * Returns whether the cartridge contains a memory bank controller.
     *
     * @return {@code true} if the cartridge contains an MBC chip
     */
    public boolean hasMemoryBankController() {
      return this != ROM_ONLY;
    }
  }

  /**
   * The header of a Game Boy cartridge.
   */
  interface Header {

    /**
     * Returns the address to which the console's boot procedure jumps after initialization.
     *
     * <p>The entry point area is a 4-byte region whose sole purpose is to jump to this address.
     * It usually contains a {@code NOP} instruction followed by a {@code JP} or {@code CALL}.
     *
     * @return the entry point address
     * @throws IllegalStateException if the entry point area contains no {@code JP} or {@code CALL}
     *         instruction
     */
    short entryPoint();

    /**
     * Sets the entry point address.
     * This writes a {@code NOP} instruction followed by an unconditional jump
     * to the given address.
     *
     * @param entryPoint the entry point address
     */
    void setEntryPoint(final short entryPoint);

    /**
     * The number of bytes reserved for the Nintendo logo.
     */
    int LOGO_LENGTH = 48;

    /**
     * Returns the Nintendo logo expected by the console.
     *
     * @return a copy of the valid logo
     * @see #logo() for details on verification
     */
    static byte[] getValidLogo() {
      return GBCartridgeHeaderImpl.VALID_LOGO.clone();
    }

    /**
     * Returns the Nintendo logo bitmap.
     *
     * <p>If incorrect, the console's boot procedure locks up in an infinite loop.
     * Some console models partially verify the contents of the logo. For example,
     * the GBC only verifies the first 18 bytes of the returned array.
     *
     * @return a copy of the logo
     */
    byte[] logo();

    /**
     * Copies the Nintendo logo bitmap into the given array.
     *
     * @param dest the destination array
     * @throws IllegalArgumentException if the length of {@code dest} is not {@link #LOGO_LENGTH}
     * @see #logo() for details on verification
     */
    void logo(final byte[] dest);

    /**
     * Sets the Nintendo logo bitmap.
     *
     * @param data the logo
     * @throws IllegalArgumentException if the length of {@code data} is not {@link #LOGO_LENGTH}
     * @see #logo() for details on verification
     */
    void setLogo(final byte[] data);

    /**
     * Sets the valid Nintendo logo.
     *
     * @see #getValidLogo() to get a copy of the valid logo
     */
    void setValidLogo();

    /**
     * Returns the 16-character uppercase game title.
     *
     * @return the game title
     */
    String title();

    /**
     * Sets the game title.
     *
     * @param title the game title
     * @throws IllegalArgumentException if the value has a length different from 16, or
     *         contains non-ASCII or non-uppercase characters
     */
    void setTitle(final String title);

    /**
     * Returns the 4-character uppercase manufacturer code, only present in GBC cartridges.
     *
     * @return the manufacturer code
     */
    String manufacturer();

    /**
     * Sets the game manufacturer.
     *
     * @param manufacturer the game manufacturer
     * @throws IllegalArgumentException if the value has a length different from 4, or
     *         contains non-ASCII or non-uppercase characters
     */
    void setManufacturer(final String manufacturer);

    /**
     * Returns the value of the GBC flag.
     *
     * @return the GBC flag value
     */
    byte gbc();

    /**
     * Sets the GBC flag.
     *
     * @param value the GBC flag
     */
    void setGbc(final byte value);

    /**
     * Returns whether the game supports GBC functions.
     *
     * @return {@code true} if the game supports GBC functions
     * @see #requiresColor() to check if the game requires a GBC to run
     */
    boolean hasColorFunctions();

    /**
     * Returns whether the game requires a GBC console to run.
     *
     * @return {@code true} if the game requires a GBC to run
     */
    boolean requiresColor();

    /**
     * Returns the licensee code, which indicates the company or publisher of the game.
     *
     * @return the licensee code
     */
    short licensee();

    /**
     * Sets the licensee code, following the old format.
     *
     * @param licensee the licensee code
     */
    void setOldLicensee(final byte licensee);

    /**
     * Sets the licensee code, following the new format.
     *
     * @param licensee the licensee code
     */
    void setNewLicensee(final short licensee);

    /**
     * Returns the value of the SGB flag.
     *
     * @return the SGB flag value
     */
    byte sgb();

    /**
     * Sets the SGB flag.
     *
     * @param value the SGB flag
     */
    void setSgb(final byte value);

    /**
     * Returns whether the game supports SGB functions.
     *
     * @return {@code true} if the game supports SGB functions
     */
    boolean hasSuperFunctions();

    /**
     * Specifies the type of the cartridge.
     *
     * @return the cartridge type, or {@code null} if not found
     */
    @Nullable Type type();

    /**
     * Sets the cartridge type.
     *
     * @param type the cartridge type
     */
    void setType(final Type type);

    /**
     * Returns the ROM size code.
     *
     * @return the ROM size code
     */
    byte romSize();

    /**
     * The minimum ROM size, in bytes.
     */
    int MIN_ROM_BYTES = 1 << 15; // 32 KB

    /**
     * The maximum ROM size, in bytes.
     */
    int MAX_ROM_BYTES = 1 << 23; // 8 MB

    /**
     * Returns the ROM size.
     *
     * @return the ROM size, in bytes
     */
    int romSizeBytes();

    /**
     * Sets the ROM size code.
     *
     * <p>Note that the cartridge may not support this configuration.
     * In particular, sizes larger than {@code 32 KB} require a memory bank controller.
     *
     * @param code the ROM size code
     * @throws IllegalArgumentException if the code is invalid
     */
    void setRomSize(final byte code);

    // TODO Convert to utility
    /*
     * Sets the ROM size. The value is rounded up to the nearest power of 2 or
     * {@link #MIN_ROM_SIZE}, whichever is larger.
     *
     * <p>The constraints on {@link #writeRomSize(byte)} also apply.
     *
     * @param bytes the ROM size, in bytes
     * @throws IllegalArgumentException if the rounded up size is larger than {@link #MAX_ROM_SIZE}
     * @see #writeRomSize(byte) to set the ROM size code directly
     *
    @Deprecated
    void writeRawRomSize(final int bytes);*/

    /**
     * Returns the RAM size code.
     *
     * @return the RAM size code
     */
    byte ramSize();

    /**
     * The maximum RAM size, in bytes.
     */
    int MAX_RAM_BYTES = 1 << 17; // 128 KB

    /**
     * Returns the RAM size, if any.
     *
     * @return the RAM size, in bytes
     * @throws IllegalStateException if the current RAM size code is invalid
     */
    int ramSizeBytes();

    /**
     * Sets the RAM size code.
     *
     * <p>Note that the cartridge may not support this configuration.
     *
     * @param code the RAM size code
     * @throws IllegalArgumentException if the code is invalid
     */
    void setRamSize(final byte code);

    /**
     * Returns the destination code.
     *
     * @return the destination code
     */
    boolean destination();

    /**
     * Returns whether the cartridge is supposed to be sold in Japan.
     *
     * @return {@code true} if this is a cartridge distributed in Japan
     */
    boolean japaneseDistribution();

    /**
     * Sets the destination code.
     *
     * @param destination the destination code
     */
    void setDestination(final boolean destination);

    /**
     * Returns the game version.
     *
     * @return the version
     */
    byte version();

    /**
     * Sets the game version.
     *
     * @param version the version
     */
    void setVersion(final byte version);

    /**
     * Returns the checksum of some header data.
     * If incorrect, the console's boot procedure locks up in an infinite loop.
     *
     * @return the header checksum
     */
    byte checksum();

    /**
     * Computes the checksum of some header data (as returned by {@link #checksum()}) from
     * the current header contents.
     *
     * @return the computed header checksum
     */
    byte computeChecksum(); // TODO make utility?

    /**
     * Sets the header checksum.
     *
     * @param checksum the header checksum
     */
    void setChecksum(final byte checksum);

    /**
     * Updates the header checksum, as returned by {@link #computeChecksum()}.
     *
     * @return the computed checksum
     * @see #computeChecksum() to compute, but not overwrite, the header checksum
     */
    default byte setChecksum() {
      byte checksum = computeChecksum();
      setChecksum(checksum);
      return checksum;
    }

    /**
     * Returns the checksum of the entire ROM.
     * Unlike the header checksum, the console boots even if incorrect.
     *
     * @return the global checksum
     */
    short globalChecksum();

    /**
     * Sets the global checksum.
     *
     * @param checksum the global checksum
     */
    void setGlobalChecksum(final short checksum);
  }
}
