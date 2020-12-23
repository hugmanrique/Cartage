package me.hugmanrique.cartage.gb;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import me.hugmanrique.cartage.CartridgeTestSuite;
import me.hugmanrique.cartage.TestUtils;
import org.junit.jupiter.api.Test;

public class GBCartridgeTests extends CartridgeTestSuite<GBCartridge> {

  private static final Path SUPER_MARIO = TestUtils.getCartridge("sml.gb");

  private final GBCartridge.Header header;

  GBCartridgeTests() throws IOException {
    super(GBCartridge.read(SUPER_MARIO));
    this.header = this.cartridge.header();
  }

  @Test
  void testEntryPoint() {
    assertEquals(0x150, header.entryPoint());
    header.setEntryPoint((short) 0xABCD);
    assertEquals((short) 0xABCD, header.entryPoint());

    // Manually overwrite entry point area with NOPs
    byte[] text = new byte[4];
    cartridge.setBytes(0x100, text);
    assertThrows(IllegalStateException.class, header::entryPoint);
  }

  @Test
  void testLogo() {
    byte[] valid = GBCartridge.Header.getValidLogo();
    byte[] logo = header.logo();
    assertArrayEquals(valid, logo);

    logo = new byte[GBCartridge.Header.LOGO_LENGTH];
    header.logo(logo);
    assertArrayEquals(valid, logo);

    assertThrows(IllegalArgumentException.class, () -> {
      header.logo(new byte[1]);
    }, "Invalid length");
  }

  @Test
  void testSetLogo() {
    byte[] logo = new byte[GBCartridge.Header.LOGO_LENGTH]; // zeros
    header.setLogo(logo);
    assertArrayEquals(logo, header.logo());

    byte[] valid = GBCartridge.Header.getValidLogo();
    header.setValidLogo();
    assertArrayEquals(valid, header.logo());

    assertThrows(IllegalArgumentException.class, () -> {
      header.setLogo(new byte[64]);
    }, "Invalid length");
  }

  @Test
  void testLogoMethodReturnsCopies() {
    assertNotSame(header.logo(), header.logo());
    assertNotSame(GBCartridge.Header.getValidLogo(), GBCartridge.Header.getValidLogo());
  }

  @Test
  void testTitle() {
    assertEquals("SUPER MARIOLAND\0", header.title());
  }

  @Test
  void testSetTitle() {
    final String newTitle = "HELLO WORLD\0\0\0\0\0";
    header.setTitle(newTitle);
    assertEquals(newTitle, header.title());

    assertThrows(IllegalArgumentException.class, () ->
      header.setTitle("abc"), "16 chars");
    assertThrows(IllegalArgumentException.class, () ->
      header.setTitle("áááááááááááááááá"), "ASCII");
    assertThrows(IllegalArgumentException.class, () ->
      header.setTitle("super marioland\0"), "uppercase");
  }

  @Test
  void testManufacturer() {
    // This cartridge is old, overlaps with title
    assertEquals("LAND", header.manufacturer());
    final String newManufacturer = "ABCD";
    header.setManufacturer(newManufacturer);
    assertEquals(newManufacturer, header.manufacturer());
    assertThrows(IllegalArgumentException.class, () ->
      header.setManufacturer("abc"), "4 chars");
    assertThrows(IllegalArgumentException.class, () ->
      header.setManufacturer("éíóú"), "ASCII");
    assertThrows(IllegalArgumentException.class, () ->
      header.setManufacturer("abcd\0"), "uppercase");
  }

  @Test
  void testGbc() {
    assertEquals(0, header.gbc());
    assertFalse(header.hasColorFunctions());
    assertFalse(header.requiresColor());
    header.setGbc((byte) 0x80);
    assertEquals((byte) 0x80, header.gbc());
    assertTrue(header.hasColorFunctions());
    assertFalse(header.requiresColor());
    header.setGbc((byte) 0xC0);
    assertTrue(header.hasColorFunctions());
    assertTrue(header.requiresColor());
  }

  @Test
  void testLicensee() {
    // SML uses old licensee (Nintendo R&D1)
    assertEquals(1, header.licensee());

    header.setOldLicensee((byte) 8);
    assertEquals(8, header.licensee());

    header.setNewLicensee((short) 41);
    assertEquals(41, header.licensee());
  }

  @Test
  void testSgb() {
    assertEquals(0, header.sgb());
    assertFalse(header.hasSuperFunctions());
    header.setSgb((byte) 0x3);
    assertEquals(0x3, header.sgb());
    assertTrue(header.hasSuperFunctions());
  }

  @Test
  void testType() {
    assertEquals(GBCartridge.Type.MBC1, header.type());
    header.setType(GBCartridge.Type.ROM_ONLY);
    assertEquals(GBCartridge.Type.ROM_ONLY, header.type());
  }

  @Test
  void testRomSize() {
    assertEquals(0x1, header.romSize());
    assertEquals(1 << 16, header.romSizeBytes()); // 64 KB
    // Linear BASE_SIZE << n
    header.setRomSize((byte) 0x5);
    assertEquals(0x5, header.romSize());
    assertEquals(1 << 20, header.romSizeBytes()); // 1 MB
    // Non power-of-2
    header.setRomSize((byte) 0x53);
    assertEquals(0x53, header.romSize());
    assertEquals(0x140000, header.romSizeBytes()); // 1.2 MB (80 banks)
  }

  @Test
  void testRamSize() {
    assertEquals(0x0, header.ramSize());
    assertEquals(0, header.ramSizeBytes());
    header.setRamSize((byte) 0x3);
    assertEquals((byte) 0x3, header.ramSize());
    assertEquals(1 << 15, header.ramSizeBytes()); // 32 KB
    header.setRamSize((byte) 0x5);
    assertEquals(1 << 16, header.ramSizeBytes()); // 64 KB
  }

  @Test
  void testDestination() {
    assertFalse(header.destination());
    assertTrue(header.japaneseDistribution());
    header.setDestination(true);
    assertTrue(header.destination());
    assertFalse(header.japaneseDistribution());
  }

  @Test
  void testHeaderChecksum() {
    assertEquals((byte) 0x9E, header.checksum());
    header.setChecksum((byte) 0x12);
    assertEquals((byte) 0x12, header.checksum());
  }

  @Test
  void testComputeChecksum() {
    assertEquals((byte) 0x9E, header.computeChecksum());

    // Simulate a bit flip
    header.setDestination(true);
    assertEquals((byte) 0x9D, header.computeChecksum());
    header.setChecksum();
    assertEquals((byte) 0x9D, header.checksum());
  }

  @Test
  void testGlobalChecksum() {
    assertEquals((short) 0x416B, header.globalChecksum());
    assertEquals((short) 0x416B, cartridge.computeChecksum());
  }
}
