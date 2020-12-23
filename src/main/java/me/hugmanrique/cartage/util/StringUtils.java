package me.hugmanrique.cartage.util;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;

public final class StringUtils {

  public static void requireLength(final CharSequence sequence, final int expectedLength) {
    if (sequence.length() != expectedLength) {
      throw new IllegalArgumentException("Expected string length to be " + expectedLength
        + ", got" + sequence.length() + " instead (" + sequence + ")");
    }
  }

  // TODO Document this accepts uppercase AND numbers and symbols (e.g. HELLO123 returns true)
  public static boolean isUpperCase(final String string) {
    for (int i = 0; i < string.length(); i++) {
      if (Character.isLowerCase(string.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isAscii(final CharSequence sequence) {
    return StandardCharsets.US_ASCII.newEncoder().canEncode(sequence);
  }

  public static void requireUppercaseAscii(final String string) {
    requireNonNull(string);
    if (!isUpperCase(string)) {
      throw new IllegalArgumentException("Expected all-uppercase string, got " + string);
    }
    if (!isAscii(string)) {
      throw new IllegalArgumentException(string + " contains non-ASCII characters");
    }
  }

  private StringUtils() {
    throw new AssertionError();
  }
}
