/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests;

import static java.util.Objects.requireNonNull;

import java.nio.ByteOrder;
import jdk.incubator.foreign.MemorySegment;
import me.hugmanrique.cartage.AbstractCartridge;
import me.hugmanrique.cartage.Cartridge;

/**
 * A {@link Cartridge} with no special functionality.
 */
public class DummyCartridge extends AbstractCartridge {

  /**
   * Constructs a {@link DummyCartridge} from a memory segment.
   *
   * @param data the cartridge data
   * @param order the cartridge's byte order
   * @return a cartridge
   */
  public static DummyCartridge fromData(final MemorySegment data, final ByteOrder order) {
    return new DummyCartridge(requireNonNull(data), requireNonNull(order));
  }

  /**
   * Constructs a {@link DummyCartridge} from a byte array.
   *
   * @param data the cartridge data
   * @param order the cartridge's byte order
   * @return a cartridge
   */
  public static DummyCartridge fromData(final byte[] data, final ByteOrder order) {
    return new DummyCartridge(MemorySegment.ofArray(requireNonNull(data)), requireNonNull(order));
  }

  private DummyCartridge(final MemorySegment segment, final ByteOrder order) {
    super(segment, order);
  }
}
