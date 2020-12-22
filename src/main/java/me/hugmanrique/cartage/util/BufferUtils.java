package me.hugmanrique.cartage.util;

import static java.util.Objects.requireNonNull;

import java.nio.ByteBuffer;

/**
 * Utilities related to {@link ByteBuffer}s.
 */
public final class BufferUtils {

  /**
   * Reads and places the contents of the given buffer into a byte array.
   *
   * <p>If the given buffer is backed by an array, modifications to the buffer's content will
   * cause the array's content to be modified, and vice versa.
   *
   * @param buffer the buffer
   * @return the array containing the buffer contents
   */
  @SuppressWarnings("ByteBufferBackingArray")
  public static byte[] toByteArray(final ByteBuffer buffer) {
    requireNonNull(buffer);
    if (buffer.hasArray()) {
      return buffer.array();
    }
    byte[] data = new byte[buffer.capacity()];
    buffer.get(0, data);
    return data;
  }

  /*var channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
    ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
    return new SomeCartridgeImpl(buffer);*/

  private BufferUtils() {
    throw new AssertionError();
  }
}
