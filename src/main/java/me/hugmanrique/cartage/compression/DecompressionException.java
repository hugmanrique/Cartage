package me.hugmanrique.cartage.compression;

import me.hugmanrique.cartage.Cartridge;

/**
 * Indicates an error occurred while attempting to decompress data from a {@link Cartridge}.
 */
public class DecompressionException extends RuntimeException {

  public DecompressionException() {
  }

  public DecompressionException(final String message) {
    super(message);
  }

  public DecompressionException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public DecompressionException(final Throwable cause) {
    super(cause);
  }
}
