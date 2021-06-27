/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests.gba;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import me.hugmanrique.cartage.gba.GBACartridge;
import me.hugmanrique.cartage.tests.CartridgeTestSuite;
import me.hugmanrique.cartage.tests.TestResources;
import org.junit.jupiter.api.Test;

/**
 * Tests the default {@link GBACartridge} implementation.
 *
 * @see <a href="https://github.com/jsmolka/gba-tests">jsmolka's gba-tests</a> test suite, licensed
 *     under the MIT license
 */
public class GBACartridgeTests extends CartridgeTestSuite<GBACartridge> {

  private final GBACartridge.Header header;

  GBACartridgeTests() throws IOException {
    super(GBACartridge.read(TestResources.getResourceStream("roms/jsmolka.gba")));
    this.header = this.cartridge.header();
  }

  @Test
  void testReadEntryPoint() {
    assertEquals(0x80000C0, header.entryPoint());
  }

  @Test
  void testSetEntryPoint() {
    final int address = GBACartridge.Header.ENTRY_INSTRUCTION_ADDR + 8;
    header.setEntryPoint(address);

    assertEquals(address, header.entryPoint());
  }

  @Test
  void testSetNegativeOffsetEntryPoint() {
    final int address = GBACartridge.Header.ENTRY_INSTRUCTION_ADDR - 12;
    header.setEntryPoint(address);

    assertEquals(address, header.entryPoint());
  }

  @Test
  void testSetMinOffsetEntryPoint() {
    final int address = 0x6000010;
    header.setEntryPoint(address);

    assertEquals(address, header.entryPoint());
  }

  @Test
  void testSetMaxOffsetEntryPoint() {
    final int address = 0xA000004;
    header.setEntryPoint(address);

    assertEquals(address, header.entryPoint());
  }

  @Test
  void testSetNonWordAlignedEntryPointOffsetThrows() {
    final int address = GBACartridge.Header.ENTRY_INSTRUCTION_ADDR + 15;
    assertThrows(IllegalArgumentException.class, () -> {
      header.setEntryPoint(address);
    });
  }

  @Test
  void testSetNegativeNonWordAlignedEntryPointThrows() {
    final int address = GBACartridge.Header.ENTRY_INSTRUCTION_ADDR - 21;
    assertThrows(IllegalArgumentException.class, () -> {
      header.setEntryPoint(address);
    });
  }

  @Test
  void testSetPositiveOutOfBoundsEntryPointThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setEntryPoint(0x6000008);
    });
  }

  @Test
  void testSetNegativeOutOfBoundsEntryPointThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setEntryPoint(0xA000008);
    });
  }

  @Test
  void testInvalidEntryPointTextThrows() {
    // Write non-B{AL} instruction
    cartridge.setInt(0x0, 0xE2402001); // sub r2, r0, #1
    assertThrows(IllegalStateException.class, header::entryPoint);
  }

  @Test
  void testReadLogo() {
    final var expected = GBACartridge.Header.getValidLogo();
    final byte[] actual = header.logo();
    final var dest = new byte[GBACartridge.Header.LOGO_LENGTH];
    header.logo(dest);

    assertArrayEquals(expected, actual);
    assertArrayEquals(expected, dest);
  }

  @Test
  void testReadLogoToTooSmallArrayThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.logo(new byte[154]);
    });
  }

  @Test
  void testSetLogo() {
    final var logo = new byte[GBACartridge.Header.LOGO_LENGTH];
    header.setLogo(logo);

    assertArrayEquals(logo, header.logo());
  }

  @Test
  void testSetValidLogo() {
    final var logo = GBACartridge.Header.getValidLogo();
    header.setValidLogo();

    assertArrayEquals(logo, header.logo());
  }

  @Test
  void testSetLogoFromTooSmallArrayThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setLogo(new byte[2]);
    });
  }

  @Test
  void testLogoMethodsReturnsArrayCopies() {
    assertNotSame(header.logo(), header.logo());
    assertNotSame(GBACartridge.Header.getValidLogo(), GBACartridge.Header.getValidLogo());
  }

  @Test
  void testReadTitle() {
    assertEquals("GBA Tests\0\0\0", header.title()); // technically invalid
  }

  @Test
  void testSetTitle() {
    final String title = "HELLO WORLD\0";
    header.setTitle(title);

    assertEquals(title, header.title());
  }

  @Test
  void testSetTooLongTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setTitle("THIS IS SOME LONG TEXT");
    });
  }

  @Test
  void testSetNonAsciiTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setTitle("áááááááááááá");
    });
  }

  @Test
  void testSetNonUppercaseTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setTitle("pokemon fire");
    });
  }

  @Test
  void testSetTitlePadsValue() {
    header.setTitle("HELLO WORLD"); // length 11 < 12

    assertEquals("HELLO WORLD\0", header.title());
  }

  private void patchCode() {
    // The original ROM has an invalid code, replace by Pokémon FireRed (English) code
    this.cartridge.setAscii(0xAC, "BPRE");
  }

  @Test
  void testReadCode() {
    this.patchCode();

    assertEquals("BPRE", header.code());
    assertEquals(GBACartridge.Type.NEW, header.type());
    assertEquals("PR", header.shortTitle());
    assertEquals(GBACartridge.Destination.USA, header.destination());
  }

  @Test
  void testSetCode() {
    header.setCode("ABCS");

    assertEquals("ABCS", header.code());
    assertEquals(GBACartridge.Type.OLD, header.type());
    assertEquals("BC", header.shortTitle());
    assertEquals(GBACartridge.Destination.SPAIN, header.destination());
  }

  @Test
  void testSetTooShortCodeThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setCode("ABC");
    });
  }

  @Test
  void testSetTooLongCodeThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setCode("ABCDE");
    });
  }

  @Test
  void testSetType() {
    this.patchCode();
    header.setType(GBACartridge.Type.NES_EMULATOR);

    assertEquals(GBACartridge.Type.NES_EMULATOR, header.type());
    assertEquals("FPRE", header.code());
  }

  @Test
  void testSetShortTitle() {
    this.patchCode();
    header.setShortTitle("XY");

    assertEquals("XY", header.shortTitle());
    assertEquals("BXYE", header.code());
  }

  @Test
  void testSetTooShortShortTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setShortTitle("A");
    });
  }

  @Test
  void testSetTooLongShortTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setShortTitle("ABC");
    });
  }

  @Test
  void testSetDestination() {
    this.patchCode();
    header.setDestination(GBACartridge.Destination.EUROPE);

    assertEquals(GBACartridge.Destination.EUROPE, header.destination());
    assertEquals("BPRP", header.code());
  }

  @Test
  void testReadLicensee() {
    assertEquals("JS", header.licensee());
  }

  @Test
  void testSetLicensee() {
    header.setLicensee("01"); // Nintendo

    assertEquals("01", header.licensee());
  }

  @Test
  void testSetTooLongLicenseeThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setLicensee("012");
    });
  }

  @Test
  void testSetTooShortLicenseeThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setLicensee("0");
    });
  }

  @Test
  void testSetNonAsciiLicenseeThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setLicensee("ÀB");
    });
  }

  @Test
  void testSetNonUppercaseLicenseeThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setLicensee("ab");
    });
  }

  @Test
  void testReadRequiredConsole() {
    assertEquals(0, header.requiredConsole());
  }

  @Test
  void testSetRequiredConsole() {
    header.setRequiredConsole((byte) 0x1);

    assertEquals((byte) 0x1, header.requiredConsole());
  }

  @Test
  void testReadDacs() {
    assertNull(header.dacs());
  }

  @Test
  void testSetDacs() {
    header.setDacs(GBACartridge.DACSType.ONE_MBIT);

    assertEquals(GBACartridge.DACSType.ONE_MBIT, header.dacs());
  }

  @Test
  void testClearDacs() {
    header.setDacs(GBACartridge.DACSType.EIGHT_MBIT);
    header.clearDacs();

    assertNull(header.dacs());
  }

  @Test
  void testReadVersion() {
    assertEquals(0, header.version());
  }

  @Test
  void testSetVersion() {
    header.setVersion((byte) 0xAB);

    assertEquals((byte) 0xAB, header.version());
  }

  @Test
  void testReadChecksum() {
    assertEquals((byte) 0x69, header.checksum());
  }

  @Test
  void testSetChecksum() {
    header.setChecksum((byte) 0xF0);

    assertEquals((byte) 0xF0, header.checksum());
  }

  @Test
  void testComputeChecksum() {
    assertEquals((byte) 0x69, header.computeChecksum());
  }

  @Test
  void testComputeChecksumAfterBitFlip() {
    header.setVersion((byte) 1); // was 0

    assertEquals((byte) 0x68, header.computeChecksum());
  }

  @Test
  void testComputeAndSetChecksum() {
    header.setVersion((byte) 1); // simulate bit flip
    header.setChecksum();

    assertEquals((byte) 0x68, header.checksum());
  }
}
