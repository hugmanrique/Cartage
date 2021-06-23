/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.util;

import static java.util.Objects.requireNonNull;

/**
 * Provides {@link String}-related utilities.
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
   * Ensures the length of the given sequence is less than or equal to {@code maxLength}.
   *
   * @param sequence the sequence to check
   * @param maxLength the maximum length allowed
   * @throws IllegalArgumentException if the length of the sequence is greater than {@code
   *     maxLength}
   */
  public static void requireMaxLength(final CharSequence sequence, final int maxLength) {
    if (sequence.length() > maxLength) {
      throw new IllegalArgumentException("Expected string length to be less than " + maxLength
          + ", got " + sequence.length() + " instead (" + sequence + ")");
    }
  }

  /**
   * Ensures the given string is composed of uppercase ASCII characters, spaces, and null
   * terminators ({@code \0}).
   *
   * @param string the string to check
   * @throws IllegalArgumentException if the string contains non-ASCII or non-uppercase
   *     characters
   */
  public static void requireUppercaseAscii(final String string) {
    requireUppercaseAscii(string, true);
  }

  /**
   * Ensures the given string is composed of uppercase ASCII characters and spaces, ignoring null
   * terminators ({@code \0}) if specified.
   *
   * @param string the string to check
   * @param acceptNullTerminators whether to ignore null terminators
   * @throws IllegalArgumentException if the string contains non-ASCII or non-uppercase
   *     characters
   */
  public static void requireUppercaseAscii(final String string,
                                           final boolean acceptNullTerminators) {
    requireNonNull(string);
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i); // ignore code points outside of the BMP
      if (acceptNullTerminators && c == '\0') {
        continue;
      }
      if (c != ' ' && (c < 'A' || c > 'Z')) {
        throw new IllegalArgumentException("Expected all-uppercase ASCII string, got " + string);
      }
    }
  }

  /**
   * Returns a string of the given length, consisting of {@code string} followed by as many
   * instances of {@code padChar} as necessary to reach that length.
   *
   * @param string the string to pad
   * @param length the length of the returned string
   * @param padChar the character to append at the end of the string
   * @return the padded string
   * @throws IllegalArgumentException if {@code string.length() > length}
   */
  // TODO Change all string setters in GB(A)Cartridge to pad if necessary using this method
  public static String padEnd(final String string, int length, char padChar) {
    final int originalLength = string.length();
    if (originalLength > length) {
      throw new IllegalArgumentException(
          "Cannot pad string with length greater than padded length");
    }
    return string + String.valueOf(padChar).repeat(length - originalLength);
  }

  private StringUtils() {
    throw new AssertionError();
  }
}
