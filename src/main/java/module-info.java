/*
 * Copyright (c) 2021 Hugo Manrique.
 *
 * This work is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

module me.hugmanrique.cartage {
  exports me.hugmanrique.cartage;
  exports me.hugmanrique.cartage.compression;
  exports me.hugmanrique.cartage.gb;
  exports me.hugmanrique.cartage.gba;
  requires transitive jdk.incubator.foreign;
  requires org.checkerframework.checker.qual;
}
