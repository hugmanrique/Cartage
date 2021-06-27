# 🕹️ Cartage

[![artifact][artifact]][artifact-url]
[![javadoc][javadoc]][javadoc-url]
[![action][action]][action-url]
[![license][license]][license-url]

**Cartage** provides utilities for reading and writing retro console cartridges.

## Installation

Requires Java 16 or later.

### Gradle

```groovy
repositories {
  mavenCentral()
}

dependencies {
  compile 'me.hugmanrique:cartage:0.0.3-SNAPSHOT'
}
```

### Maven

```xml
<dependency>
  <groupId>me.hugmanrique</groupId>
  <artifactId>cartage</artifactId>
  <version>0.0.3-SNAPSHOT</version>
</dependency>
```

## Supported platforms

### Game Boy

See `GBCartridge`.

Credits:
- [Pan Docs](https://gbdev.io/pandocs/#the-cartridge-header)
- [The Cycle-Accurate Game Boy Docs](https://raw.githubusercontent.com/AntonioND/giibiiadvance/master/docs/TCAGBD.pdf)

## Game Boy Advance

See `GBACartridge`.

Credits:
- [GBATEK](http://problemkaputt.de/gbatek.htm)
- [Reiner Ziegler](https://reinerziegler.de.mirrors.gg8.se/GBA/gba.htm)

## Testing

We use the following ROMs for testing purposes:

- [gbc-hw-tests](https://github.com/AntonioND/gbc-hw-tests) by AntonioND, and
- [gba-tests](https://github.com/jsmolka/gba-tests) by jsmolka.

## License

[MIT](LICENSE) &copy; [Hugo Manrique](https://hugmanrique.me)

[artifact]: https://img.shields.io/maven-central/v/me.hugmanrique/cartage
[artifact-url]: https://search.maven.org/artifact/me.hugmanrique/cartage
[javadoc]: https://javadoc.io/badge2/me.hugmanrique/cartage/javadoc.svg
[javadoc-url]: https://javadoc.io/doc/me.hugmanrique/cartage
[action]: https://github.com/hugmanrique/Cartage/actions/workflows/build.yml/badge.svg
[action-url]: https://github.com/hugmanrique/Cartage/actions/workflows/build.yml
[license]: https://img.shields.io/github/license/hugmanrique/Cartage.svg
[license-url]: LICENSE
