# 🕹️ Cartage

[![artifact][artifact]][artifact-url]
[![javadoc][javadoc]][javadoc-url]
[![tests][tests]][tests-url]
[![license][license]][license-url]

**Cartage** provides utilities for reading and writing retro console cartridges.

## Installation

Requires Java 15 or later.

### Maven

```xml
<dependency>
  <groupId>me.hugmanrique</groupId>
  <artifactId>cartage</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

```groovy
repositories {
  mavenCentral()
}

dependencies {
  compile 'me.hugmanrique:cartage:0.0.1-SNAPSHOT'
}
```

## Supported platforms

### Game Boy

See `GBCartridge`.

Credits:
- [Pan Docs](https://gbdev.io/pandocs/#the-cartridge-header)
- [The Cycle-Accurate Game Boy Docs](https://raw.githubusercontent.com/AntonioND/giibiiadvance/master/docs/TCAGBD.pdf)

## Testing

Some tests expect the following game dumps to be available under the `roms/` directory:

| Game name        | Platform | File name |
|------------------|----------|-----------|
| Super Mario Land | Game Boy | sml.gb    |

Redistributing these files is illegal, so you need to dump these games by yourself.

## License

[MIT](LICENSE) &copy; [Hugo Manrique](https://hugmanrique.me)

[artifact]: https://img.shields.io/maven-central/v/me.hugmanrique/cartage
[artifact-url]: https://search.maven.org/artifact/me.hugmanrique/cartage
[javadoc]: https://javadoc.io/badge2/me.hugmanrique/cartage/javadoc.svg
[javadoc-url]: https://javadoc.io/doc/me.hugmanrique/cartage
[tests]: https://img.shields.io/travis/hugmanrique/Cartage/main.svg
[tests-url]: https://travis-ci.org/hugmanrique/Cartage
[license]: https://img.shields.io/github/license/hugmanrique/Cartage.svg
[license-url]: LICENSE
