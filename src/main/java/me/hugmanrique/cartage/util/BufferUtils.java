package me.hugmanrique.cartage.util;

/**
 * Provides memory handling-related utilities.
 */
public final class BufferUtils {

  /**
   * Copies the contents from the specified array, beginning at the specified position,
   * byte-by-byte, to the specified position of the array. The source and destination areas may
   * overlap: a copy operation observes the results of past operations (unlike {@link
   * System#arraycopy(Object, int, Object, int, int)}).
   *
   * @param arr the source and destination array
   * @param srcPos the starting source position in the array
   * @param destPos the starting destination position in the array
   * @param length the number of array elements to copy
   * @implNote fallbacks to {@link System#arraycopy(Object, int, Object, int, int)} if the
   *     source and destination ranges do not overlap.
   */
  public static void copyByteByByte(final byte[] arr, final int srcPos, final int destPos,
                                    final int length) {
    if ((srcPos + length) <= destPos || (destPos + length) <= srcPos) {
      System.arraycopy(arr, srcPos, arr, destPos, length);
    } else {
      //noinspection ManualArrayCopy
      for (int i = 0; i < length; i++) {
        arr[destPos + i] = arr[srcPos + i];
      }
    }
  }

  private BufferUtils() {
    throw new AssertionError();
  }
}
