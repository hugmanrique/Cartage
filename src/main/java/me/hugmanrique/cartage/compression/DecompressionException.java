/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import me.hugmanrique.cartage.Cartridge;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Indicates an error occurred while attempting to decompress data from a {@link Cartridge}.
 */
public class DecompressionException extends RuntimeException {

  /**
   * Constructs a decompression exception with the given detail message and no cause.
   *
   * @param message the detail message
   */
  public DecompressionException(final String message) {
    super(message);
  }

  /**
   * Constructs a decompression exception with the given detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public DecompressionException(final String message, final @Nullable Throwable cause) {
    super(message, cause);
  }
}
