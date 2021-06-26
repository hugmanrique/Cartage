package me.hugmanrique.cartage.tests.gb;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import me.hugmanrique.cartage.gb.GBCartridge;
import me.hugmanrique.cartage.tests.CartridgeTestSuite;
import me.hugmanrique.cartage.tests.TestResources;
import org.junit.jupiter.api.Test;

/**
 * Tests the default {@link GBCartridge} implementation.
 *
 * @see <a href="https://github.com/AntonioND/gbc-hw-tests">AntonioND's gbc-hw-tests</a> ROM suite
 */
public class NewGBCartridgeTests extends CartridgeTestSuite<GBCartridge> {

  private final GBCartridge.Header header;

  NewGBCartridgeTests() throws IOException {
    super(GBCartridge.read(TestResources.getResourceStream("roms/AntonioND.gbc")));
    this.header = this.cartridge.header();
  }

  @Test
  void testReadEntrypoint() {
    assertEquals(0x4E, header.entryPoint()); // JR 0x4C
  }

  @Test
  void testWriteEntrypoint() {
    header.setEntryPoint((short) 0xABCD);

    assertEquals((short) 0xABCD, header.entryPoint());
  }

  @Test
  void testInvalidEntrypointBytesThrows() {
    final var text = new byte[4];
    cartridge.setBytes(0x100, text);

    assertThrows(IllegalStateException.class, header::entryPoint);
  }

  @Test
  void testReadLogo() {
    final var expected = GBCartridge.Header.getValidLogo();
    final byte[] actual = header.logo();
    final var dest = new byte[GBCartridge.Header.LOGO_LENGTH];
    header.logo(dest);

    assertArrayEquals(expected, actual);
    assertArrayEquals(expected, dest);
  }

  @Test
  void testReadLogoToTooSmallArrayThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.logo(new byte[1]);
    });
  }

  @Test
  void testSetLogo() {
    final var logo = new byte[GBCartridge.Header.LOGO_LENGTH];
    header.setLogo(logo);

    assertArrayEquals(logo, header.logo());
  }

  @Test
  void testSetValidLogo() {
    final var logo = GBCartridge.Header.getValidLogo();
    header.setValidLogo();

    assertArrayEquals(logo, header.logo());
  }

  @Test
  void testSetLogoFromTooSmallArrayThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setLogo(new byte[64]);
    });
  }

  @Test
  void testLogoMethodsReturnArrayCopies() {
    assertNotSame(header.logo(), header.logo());
    assertNotSame(GBCartridge.Header.getValidLogo(), GBCartridge.Header.getValidLogo());
  }

  @Test
  void testReadTitle() {
    assertEquals("CPU REG START\0\0\0", header.title());
  }

  @Test
  void testSetTitle() {
    final String title = "HELLO WORLD\0\0\0\0\0";
    header.setTitle(title);

    assertEquals(title, header.title());
  }

  @Test
  void testSetTooLongTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setTitle("ABCDEFGHIJKLMNOPQ");
    });
  }

  @Test
  void testSetNonAsciiTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setTitle("áááááááááááááááá");
    });
  }

  @Test
  void testSetNonUppercaseTitleThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setTitle("super marioland\0");
    });
  }

  @Test
  void testSetTitlePadsValue() {
    header.setTitle("FOO BAR BAZ"); // length 11 < 16

    assertEquals("FOO BAR BAZ\0\0\0\0\0", header.title());
  }

  @Test
  void testReadManufacturer() {
    assertEquals("RT\0\0", header.manufacturer());
  }

  @Test
  void testSetManufacturer() {
    final String manufacturer = "ABCD";
    header.setManufacturer(manufacturer);

    assertEquals(manufacturer, header.manufacturer());
  }

  @Test
  void testSetTooLongManufacturerThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setManufacturer("ABCDE");
    });
  }

  @Test
  void testSetNonAsciiManufacturerThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setManufacturer("éíóú");
    });
  }

  @Test
  void testSetNonUppercaseManufacturerThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      header.setManufacturer("abcd");
    });
  }

  @Test
  void testSetManufacturerPadsValue() {
    header.setManufacturer("AB"); // length 2 < 4

    assertEquals("AB\0\0", header.manufacturer());
  }

  @Test
  void testReadGbc() {
    assertEquals(0, header.gbc());
    assertFalse(header.hasColorFunctions());
    assertFalse(header.requiresColor());
  }

  @Test
  void testSetHasColorGbc() {
    header.setGbc((byte) 0x80);

    assertEquals((byte) 0x80, header.gbc());
    assertTrue(header.hasColorFunctions());
    assertFalse(header.requiresColor());
  }

  @Test
  void testSetRequiresColorGbc() {
    header.setGbc((byte) 0xC0);

    assertEquals((byte) 0xC0, header.gbc());
    assertTrue(header.hasColorFunctions());
    assertTrue(header.requiresColor());
  }

  @Test
  void testReadLicensee() {
    assertEquals(0, header.licensee()); // None (uses old)
  }

  @Test
  void testSetOldLicensee() {
    header.setOldLicensee((byte) 0x8); // Capcom

    assertEquals(0x8, header.licensee());
  }

  @Test
  void testSetNewLicensee() {
    header.setNewLicensee((byte) 0x29); // Ubisoft

    assertEquals((byte) 0x29, header.licensee());
  }

  @Test
  void testReadSgb() {
    assertEquals(0, header.sgb());
    assertFalse(header.hasSuperFunctions());
  }

  @Test
  void testSetSgb() {
    header.setSgb((byte) 0x3);

    assertEquals(0x3, header.gbc());
    assertTrue(header.hasSuperFunctions());
  }

  @Test
  void testReadType() {
    assertEquals(GBCartridge.Type.MBC5_RAM_BATTERY, header.type());
  }

  @Test
  void testSetType() {
    header.setType(GBCartridge.Type.ROM_ONLY);

    assertEquals(GBCartridge.Type.ROM_ONLY, header.type());
  }

  @Test
  void testReadRomSize() {
    assertEquals(0, header.romSize());
    assertEquals(1 << 15, header.romSizeBytes()); // 32 KB
  }

  @Test
  void testSetRomSize() {
    header.setRomSize((byte) 0x5);

    assertEquals(0x5, header.romSize());
    assertEquals(1 << 20, header.romSizeBytes()); // 1 MB
  }

  @Test
  void testSetNonPowerOf2RomSize() {
    header.setRomSize((byte) 0x53);

    assertEquals(0x53, header.romSize());
    assertEquals(0x140000, header.romSizeBytes()); // 1.2 MB (80 banks)
  }

  @Test
  void testReadRamSize() {

  }
}
