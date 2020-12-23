package me.hugmanrique.cartage.gba;

import static java.util.Objects.requireNonNull;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The default {@link GBACartridge.Header} implementation.
 */
final class GBACartridgeHeaderImpl implements GBACartridge.Header {

  private final GBACartridge cartridge;

  public GBACartridgeHeaderImpl(final GBACartridge cartridge) {
    this.cartridge = requireNonNull(cartridge);
  }

  @Override
  public int entryPoint() {
    return 0;
  }

  @Override
  public void setEntryPoint(final int address) {

  }

  @Override
  public byte[] logo() {
    return new byte[0];
  }

  @Override
  public void logo(final byte[] dest) {

  }

  @Override
  public void setLogo(final byte[] source) {

  }

  @Override
  public void setValidLogo() {

  }

  @Override
  public String title() {
    return null;
  }

  @Override
  public void setTitle(final String title) {

  }

  @Override
  public String code() {
    return null;
  }

  @Override
  public void setCode(final String code) {

  }

  @Override
  public GBACartridge.@Nullable Type type() {
    return null;
  }

  @Override
  public void setType(final GBACartridge.Type type) {

  }

  @Override
  public String shortTitle() {
    return null;
  }

  @Override
  public void setShortTitle(final String value) {

  }

  @Override
  public GBACartridge.@Nullable Destination destination() {
    return null;
  }

  @Override
  public void setDestination(final GBACartridge.Destination destination) {

  }

  @Override
  public short licensee() {
    return 0;
  }

  @Override
  public void setLicensee(final short licensee) {

  }

  @Override
  public GBACartridge.@Nullable DACSType dacs() {
    return null;
  }

  @Override
  public void setDacs(final GBACartridge.DACSType type) {

  }

  @Override
  public byte version() {
    return 0;
  }

  @Override
  public void setVersion(final byte version) {

  }

  @Override
  public byte checksum() {
    return 0;
  }

  @Override
  public byte computeChecksum() {
    return 0;
  }

  @Override
  public void setChecksum(final byte checksum) {

  }

  @Override
  public byte setChecksum() {
    return 0;
  }
}
