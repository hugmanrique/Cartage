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
  compile 'me.hugmanrique:cartage:0.0.1-SNAPSHOT'
}
```

### Maven

```xml
<dependency>
  <groupId>me.hugmanrique</groupId>
  <artifactId>cartage</artifactId>
  <version>0.0.1-SNAPSHOT</version>
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

Some tests expect the following game dumps to be available under the `roms/` directory:

| Game name | Platform | File name |
|-----------|----------|-----------|
| Super Mario Land | Game Boy | sml.gb |
| Pokémon FireRed (English) | Game Boy Advance | pfr.gba |

Redistributing these files is illegal, so you need to dump these games by yourself.

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
