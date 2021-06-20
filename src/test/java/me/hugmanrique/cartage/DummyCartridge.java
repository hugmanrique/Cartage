/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage;

import static java.util.Objects.requireNonNull;

import java.nio.ByteOrder;
import jdk.incubator.foreign.MemorySegment;

/**
 * A {@link Cartridge} with no special functionality.
 */
public class DummyCartridge extends AbstractCartridge {

  /**
   * Constructs a {@link DummyCartridge}.
   *
   * @param data the cartridge data
   * @param order the cartridge's byte order
   */
  public static DummyCartridge fromData(final byte[] data, final ByteOrder order) {
    return new DummyCartridge(requireNonNull(data), requireNonNull(order));
  }

  private DummyCartridge(final byte[] data, final ByteOrder order) {
    super(MemorySegment.ofArray(data), order);
  }
}
