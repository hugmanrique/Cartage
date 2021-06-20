/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package me.hugmanrique.cartage.tests;

import static java.util.Objects.requireNonNull;

import java.nio.ByteBuffer;
import jdk.incubator.foreign.MemorySegment;
import me.hugmanrique.cartage.Cartridge;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

/**
 * Tests the behavior of a {@link Cartridge} type.
 *
 * @param <T> the cartridge type
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CartridgeTestSuite<T extends Cartridge> {

  protected final T cartridge;
  private final MemorySegment original; // used to restore contents after each test

  protected CartridgeTestSuite(final T cartridge) {
    this.cartridge = requireNonNull(cartridge);
    this.original = MemorySegment.ofByteBuffer(ByteBuffer.allocate((int) cartridge.size()));
    this.cartridge.copyTo(this.original);
  }

  protected void reset() {
    this.cartridge.copyFrom(this.original);
  }

  @BeforeEach
  void beforeAll() {
    this.reset();
  }

  @AfterAll
  void afterAll() {
    this.cartridge.close();
  }
}
