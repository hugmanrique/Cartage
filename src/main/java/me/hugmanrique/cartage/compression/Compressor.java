/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.compression;

import me.hugmanrique.cartage.Cartridge;

/**
 * Compresses data from a {@link Cartridge}.
 *
 * <p>Unless otherwise noted, implementations are thread-safe.
 */
public interface Compressor {

  byte[] compress(final Cartridge cartridge, final long offset, final int length);

  default byte[] compress(final Cartridge cartridge, final int length) {
    return this.compress(cartridge, cartridge.offset(), length);
  }
}
