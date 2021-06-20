/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.gba;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import me.hugmanrique.cartage.Cartridge;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A Game Boy Advance cartridge.
 *
 * @see <a href="http://problemkaputt.de/gbatek.htm">GBATEK</a>
 * @see <a href="https://reinerziegler.de.mirrors.gg8.se/GBA/gba.htm">GBA</a> by R. Ziegler
 */
public interface GBACartridge extends Cartridge {

  /**
   * Reads a cartridge from the given path.
   *
   * @param path the path
   * @return the read cartridge
   * @throws IOException if an I/O error occurs
   */
  static GBACartridge read(final Path path) throws IOException {
    requireNonNull(path);
    byte[] data = Files.readAllBytes(path);
    return new GBACartridgeImpl(data);
  }

  /**
   * Reads a cartridge from the given stream. The stream is not closed.
   *
   * @param stream the input stream
   * @return the read cartridge
   * @throws IOException if an I/O error occurs
   */
  static GBACartridge read(final InputStream stream) throws IOException {
    requireNonNull(stream);
    byte[] data = stream.readAllBytes();
    return new GBACartridgeImpl(data);
  }

  /**
   * Returns the cartridge header.
   *
   * @return the header
   */
  Header header();

  /**
   * Specifies additional hardware present in the cartridge.
   */
  enum Type {
    OLD('A'),
    NEW('B'),
    FUTURE('C'), // unused
    NES_EMULATOR('F'),
    ACCELEROMETER('K'),
    E_READER('P'),
    RUMBLE_GYRO('R'),
    RTC_SOLAR('U'),
    RUMBLE('V');

    /**
     * Returns the cartridge type identified by the given value.
     *
     * @param value the type identifier
     * @return the type with the given identifier, or {@code null} if unknown
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

    Type(final char value) {
      this.value = (byte) value;
    }

    /**
     * Returns the identifier for this cartridge type.
     *
     * @return the identifier
     */
    public byte value() {
      return value;
    }
  }

  /**
   * The world region where a cartridge is distributed.
   */
  enum Destination {
    JAPAN('J'),
    USA('E'),
    EUROPE('P'),
    GERMANY('D'),
    FRANCE('F'),
    ITALY('I'),
    SPAIN('S');

    /**
     * Returns the destination identified by the given value.
     *
     * @param value the destination identifier
     * @return the destination with the given identifier, or {@code null} if unknown
     */
    public static @Nullable Destination of(final byte value) {
      for (var dest : values()) {
        if (dest.value() == value) {
          return dest;
        }
      }
      return null;
    }

    private final byte value;

    Destination(final char value) {
      this.value = (byte) value;
    }

    /**
     * Returns the identifier for this destination.
     *
     * @return the identifier
     */
    public byte value() {
      return value;
    }
  }

  /**
   * Indicates the entry point and size of the Debugging and Communication System (DACS) memory,
   * used by the Nintendo hardware debugger when the debugging handler is enabled.
   *
   * <p>In practice, most cartridges do not contain any memory at these addresses.
   *
   * @see Header#logo() to learn how to enable the debugging handler
   */
  enum DACSType {
    ONE_MBIT((byte) 0x40, 0x20000, 0x9FE2000),
    EIGHT_MBIT((byte) 0, 0x100000, 0x9FFC000);

    /**
     * Returns the DACS type identified by the given value.
     *
     * @param value the DACS type identifier
     * @return the type with the given identifier, or {@code null} if unknown
     */
    public static @Nullable DACSType of(final byte value) {
      for (var type : values()) {
        if (type.value() == value) {
          return type;
        }
      }
      return null;
    }

    private final byte value;
    private final int size;
    private final int entryPoint;

    DACSType(final byte value, final int size, final int entryPoint) {
      this.value = value;
      this.size = size;
      this.entryPoint = entryPoint;
    }

    /**
     * Returns the identifier for this DACS type.
     *
     * @return the identifier
     */
    public byte value() {
      return value;
    }

    /**
     * Returns the DACS flash memory size.
     *
     * @return the memory size, in bytes
     */
    public int size() {
      return size;
    }

    /**
     * Returns the debugging handler's entry point address.
     *
     * @return the DACS entry point address, in bytes
     */
    public int entryPoint() {
      return entryPoint;
    }
  }

  /**
   * The header of a Game Boy Advance cartridge. Changes to the header are reflected in the
   * cartridge, and vice-versa.
   */
  interface Header {
    // ARM7TDMI reference https://developer.arm.com/documentation/ddi0210/c/

    /**
     * The address at which a {@code B} instruction branching to the start address of the cartridge
     * is written.
     */
    int ENTRY_INSTRUCTION_ADDR = 0x8000000;

    /**
     * Returns the address to which the console's boot procedure jumps after initialization.
     *
     * @return the entry point address, in bytes
     * @throws IllegalStateException if the entry point instruction is not a {@code B}
     *     instruction
     */
    int entryPoint();

    /**
     * Sets the entry point address. This writes a {@code B} instruction at {@link
     * #ENTRY_INSTRUCTION_ADDR} with a relative word offset to the given address.
     *
     * <p>The ARM7TDMI branch instruction contain a signed 24-bit word offset. Therefore,
     * the minimum and maximum entry point addresses are {@code 0x6000004} and {@code 0x9FFFFFC}
     * respectively.
     *
     * @param address the entry point address, in bytes
     * @throws IllegalArgumentException if the given address is out of bounds, or is not word
     *     aligned, i.e. a multiple of 4
     */
    void setEntryPoint(final int address);

    /**
     * The number of bytes reserved for the Nintendo logo.
     */
    int LOGO_LENGTH = 156;

    /**
     * Returns the Nintendo logo expected by the console, with debugging mode disabled.
     *
     * @return a copy of the valid logo
     * @see #logo() for details on verification
     */
    static byte[] getValidLogo() {
      return GBACartridgeHeaderImpl.VALID_LOGO.clone();
    }

    /**
     * Returns the Nintendo logo bitmap.
     *
     * <p>If incorrect, the console's boot procedure locks up in an infinite loop.
     * The only exception are bits 2 and 7 of the 152-th byte ({@code 0x98}). When both bits are
     * set, the console's debugging mode is enabled.
     *
     * @return a copy of the logo
     */
    byte[] logo();

    /**
     * Copies the Nintendo logo into the given array.
     *
     * @param dest the destination array
     * @throws IllegalArgumentException if the length of {@code dest} is not {@link
     *     #LOGO_LENGTH}
     * @see #logo() for details on verification
     */
    void logo(final byte[] dest);

    /**
     * Sets the Nintendo logo bitmap.
     *
     * @param source the logo
     * @throws IllegalArgumentException if the length of {@code source} is not {@link
     *     #LOGO_LENGTH}
     * @see #logo() for details on verification
     */
    void setLogo(final byte[] source);

    /**
     * Sets the valid Nintendo logo, with debugging mode disabled.
     *
     * @see #getValidLogo() to get a copy of a valid logo
     */
    void setValidLogo();

    /**
     * Returns the 12-character uppercase game title.
     *
     * @return the game title
     */
    String title();

    /**
     * Sets the game title.
     *
     * @param title the game title
     * @throws IllegalArgumentException if the value has a length greater than 12, or contains
     *     non-ASCII or non-uppercase characters
     */
    void setTitle(final String title);

    // Game code

    /**
     * Returns the 4-character uppercase UTTD code.
     *
     * @return the UTTD code
     * @see #type() to get the cartridge type
     * @see #shortTitle() to get the short title
     * @see #destination() to get the destination
     */
    String code();

    /**
     * Sets the UTTD code.
     *
     * @param code the UTTD code
     * @throws IllegalArgumentException if the value has a length different from 4, or is
     *     invalid
     */
    void setCode(final String code);

    /**
     * Returns the type of the cartridge, derived from the UTTD code.
     *
     * @return the cartridge type, or {@code null} if unknown
     */
    @Nullable Type type();

    /**
     * Sets the cartridge type.
     *
     * @param type the cartridge type
     */
    void setType(final Type type);

    /**
     * Returns the 2-character uppercase short game title, derived from the UTTD code.
     *
     * @return the short game title
     */
    String shortTitle();

    /**
     * Sets the short game title.
     *
     * @param value the short game title
     * @throws IllegalArgumentException if the value as a length different from 2, or contains
     *     non-ASCII or non-uppercase characters
     */
    void setShortTitle(final String value);

    /**
     * Returns the cartridge distribution region, derived from the UTTD code.
     *
     * @return the destination, or {@code null} if unknown
     */
    @Nullable Destination destination();

    /**
     * Sets the distribution region.
     *
     * @param destination the destination
     */
    void setDestination(final Destination destination);

    /**
     * Returns the licensee code, which indicates the company or publisher of the game.
     *
     * @return the licensee code
     */
    short licensee();

    /**
     * Sets the licensee code.
     *
     * @param licensee the licensee code
     */
    void setLicensee(final short licensee);

    // Skip main unit code, always 0

    /**
     * Returns the type of the DACS memory, if any.
     *
     * @return the DACS memory type, or {@code null} if unknown
     */
    @Nullable DACSType dacs();

    /**
     * Sets the DACS memory type.
     *
     * @param type the DACS memory type
     */
    void setDacs(final DACSType type);

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
     * Returns the checksum of some header data. If incorrect, the console's boot procedure locks up
     * in an infinite loop.
     *
     * @return the header checksum
     */
    byte checksum();

    /**
     * Computes the checksum of some header data (as returned by {@link #checksum()}) from the
     * current header contents.
     *
     * @return the computed header checksum
     */
    byte computeChecksum();

    /**
     * Sets the header checksum.
     *
     * @param checksum the header checksum
     */
    void setChecksum(final byte checksum);

    /**
     * Sets the header checksum to the value returned by {@link #computeChecksum()}.
     *
     * @return the computed checksum
     * @see #computeChecksum() to compute, but not overwrite, the header checksum
     */
    byte setChecksum();
  }
}
