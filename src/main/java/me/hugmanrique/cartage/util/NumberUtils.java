package me.hugmanrique.cartage.util;

/**
 * Provides methods that operate on numeric values.
 *
 * @see <a href="https://graphics.stanford.edu/~seander/bithacks.html">Bit Twiddling Hacks</a> by S.
 *     E. Anderson
 */
public final class NumberUtils {

  /**
   * Returns whether the given value is a power of 2.
   *
   * @param value the value to check
   * @return true if the value is a power of 2; false otherwise
   */
  public static boolean isPowerOf2(final int value) {
    // For example, 0b100 & 0b011 == 0
    return (value & (value - 1)) == 0;
  }

  private NumberUtils() {
    throw new AssertionError();
  }
}
