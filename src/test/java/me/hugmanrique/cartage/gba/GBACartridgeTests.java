package me.hugmanrique.cartage.gba;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import me.hugmanrique.cartage.CartridgeTestSuite;
import me.hugmanrique.cartage.TestUtils;
import org.junit.jupiter.api.Test;

public class GBACartridgeTests extends CartridgeTestSuite<GBACartridge> {

  private static final Path POKEMON_FIRERED = TestUtils.getCartridge("pfr.gba");

  private final GBACartridge.Header header;

  GBACartridgeTests() throws IOException {
    super(GBACartridge.read(POKEMON_FIRERED));
    this.header = this.cartridge.header();
  }

  @Test
  void testEntryPoint() {
    final int baseAddress = GBACartridge.Header.ENTRY_INSTRUCTION_ADDR;
    assertEquals(baseAddress + 0x7F * 4, header.entryPoint());
    header.setEntryPoint(baseAddress + 8);
    assertEquals(baseAddress + 8, header.entryPoint());
    header.setEntryPoint(baseAddress - 12);
    assertEquals(baseAddress - 12, header.entryPoint(), "negative offset");
    header.setEntryPoint(0x6000004); // min
    assertEquals(0x6000004, header.entryPoint(), "big negative offset");
    header.setEntryPoint(0x9FFFFFC); // max
    assertEquals(0x9FFFFFC, header.entryPoint(), "big positive offset");

    assertThrows(IllegalArgumentException.class, () ->
      header.setEntryPoint(baseAddress + 15), "non-word-aligned");
    assertThrows(IllegalArgumentException.class, () ->
      header.setEntryPoint(baseAddress - 21), "negative non-word-aligned");
    assertThrows(IllegalArgumentException.class, () ->
      header.setEntryPoint(0x6000000), "out of bounds");
    assertThrows(IllegalArgumentException.class, () ->
      header.setEntryPoint(0xA000000), "out of bounds");

    // Write non-B{AL} instruction
    cartridge.setInt(0x0, 0xE2402001); // sub r2, r0, #1
    assertThrows(IllegalStateException.class, header::entryPoint);
  }

  @Test
  void testLogo() {
    byte[] valid = GBACartridge.Header.getValidLogo();
    byte[] logo = header.logo();
    assertArrayEquals(valid, logo);

    logo = new byte[GBACartridge.Header.LOGO_LENGTH];
    header.logo(logo);
    assertArrayEquals(valid, logo);

    assertThrows(IllegalArgumentException.class, () -> {
      header.logo(new byte[154]);
    }, "Invalid length");
  }

  @Test
  void testSetLogo() {
    byte[] logo = new byte[GBACartridge.Header.LOGO_LENGTH]; // zeros
    header.setLogo(logo);
    assertArrayEquals(logo, header.logo());

    byte[] valid = GBACartridge.Header.getValidLogo();
    header.setValidLogo();
    assertArrayEquals(valid, header.logo());

    assertThrows(IllegalArgumentException.class, () -> {
      header.setLogo(new byte[2]);
    }, "Invalid length");
  }

  @Test
  void testLogoMethodReturnsCopies() {
    assertNotSame(header.logo(), header.logo());
    assertNotSame(GBACartridge.Header.getValidLogo(), GBACartridge.Header.getValidLogo());
  }

  @Test
  void testTitle() {
    assertEquals("POKEMON FIRE", header.title());
  }

  @Test
  void testSetTitle() {
    final String newTitle = "HELLO WORLD\0";
    header.setTitle(newTitle);
    assertEquals(newTitle, header.title());

    assertThrows(IllegalArgumentException.class, () ->
      header.setTitle("abc"), "12 chars");
    assertThrows(IllegalArgumentException.class, () ->
      header.setTitle("áááááááááááá"), "ASCII");
    assertThrows(IllegalArgumentException.class, () ->
      header.setTitle("pokemon fire"), "uppercase");
  }

  @Test
  void testCode() {
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

    header.setType(GBACartridge.Type.NES_EMULATOR);
    assertEquals(GBACartridge.Type.NES_EMULATOR, header.type());
    assertEquals("FBCS", header.code());

    header.setShortTitle("XY");
    assertEquals("XY", header.shortTitle());
    assertEquals("FXYS", header.code());

    header.setDestination(GBACartridge.Destination.EUROPE);
    assertEquals(GBACartridge.Destination.EUROPE, header.destination());
    assertEquals("FXYP", header.code());
  }

  @Test
  void testLicensee() {
    assertEquals((short) 0x3130, header.licensee());
  }

  @Test
  void testSetLicensee() {
    header.setLicensee((short) 0);
    assertEquals(0, header.licensee());

    header.setLicensee((short) 0xBEEF);
    assertEquals((short) 0xBEEF, header.licensee());
  }

  @Test
  void testDacs() {
    assertEquals(GBACartridge.DACSType.EIGHT_MBIT, header.dacs()); // this is a lie

    header.setDacs(GBACartridge.DACSType.ONE_MBIT);
    assertEquals(GBACartridge.DACSType.ONE_MBIT, header.dacs());
  }

  @Test
  void testVersion() {
    assertEquals(1, header.version());

    header.setVersion((byte) 0xAB);
    assertEquals((byte) 0xAB, header.version());
  }

  @Test
  void testChecksum() {
    assertEquals((byte) 0x67, header.checksum());

    header.setChecksum((byte) 0xF0);
    assertEquals((byte) 0xF0, header.checksum());
  }

  @Test
  void testComputeChecksum() {
    assertEquals((byte) 0x67, header.computeChecksum());

    // Simulate a bit flip
    header.setVersion((byte) 0); // was 1
    assertEquals((byte) 0x68, header.computeChecksum());
    header.setChecksum();
    assertEquals((byte) 0x68, header.computeChecksum());
  }
}
