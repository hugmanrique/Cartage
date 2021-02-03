package me.hugmanrique.cartage.util;

import static java.util.Objects.requireNonNull;

/**
 * {@link String}-related utilities.
 */
public final class StringUtils {

  /**
   * Ensures the length of the given sequence is {@code length}.
   *
   * @param sequence the sequence to check
   * @param length the expected length
   * @throws IllegalArgumentException if the length of the sequence is not {@code length}
   */
  public static void requireLength(final CharSequence sequence, final int length) {
    if (sequence.length() != length) {
      throw new IllegalArgumentException("Expected string length to be " + length
        + ", got" + sequence.length() + " instead (" + sequence + ")");
    }
  }

  /**
   * Ensures the given string is composed of uppercase ASCII characters.
   *
   * @param string the string to check
   * @throws IllegalArgumentException if the string contains non-ASCII or non-uppercase characters
   */
  public static void requireUppercaseAscii(final String string) {
    requireNonNull(string);
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i); // ignore code points outside of the BMP
      if (c < 'A' || c > 'Z') {
        throw new IllegalArgumentException("Expected all-uppercase ASCII string, got " + string);
      }
    }
  }

  private StringUtils() {
    throw new AssertionError();
  }
}
