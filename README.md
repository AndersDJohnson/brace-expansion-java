# brace-expansion

[![Build Status](https://travis-ci.org/AndersDJohnson/brace-expansion-java.png)](https://travis-ci.org/AndersDJohnson/brace-expansion-java)
[ ![Download](https://img.shields.io/maven-central/v/me.andrz/brace-expansion.svg) ][download]

[Brace expansion] from Bash/shell in Java.

## Features
* Brace nesting.
* Ranges
  * Both integers and characters, including Unicode.
  * Increment ascending or descending.
  * Optional explicit increment step values, positive or negative.

## TODO
* Zero padded number expansion.

## Use

For example:

```java
import me.andrz.brace.BraceExpansion;
// ...
List<String> strs = BraceExpansion.expand("pic{{,s},ture{,s}}-{0..2}.{jpg,png}");
```

generates this list of strings:

```
pic-0.jpg
pics-0.jpg
picture-0.jpg
pictures-0.jpg
pic-1.jpg
pics-1.jpg
picture-1.jpg
pictures-1.jpg
pic-2.jpg
pics-2.jpg
picture-2.jpg
pictures-2.jpg
pic-0.png
pics-0.png
picture-0.png
pictures-0.png
pic-1.png
pics-1.png
picture-1.png
pictures-1.png
pic-2.png
pics-2.png
picture-2.png
pictures-2.png
```

This project provides an [ANTLR][] 4 grammar, [BraceExpansion.g4][].


## Install

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>me.andrz</groupId>
        <artifactId>brace-expansion</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle

```gradle
repositories {
    mavenCentral()
}

dependencies {
    compile 'me.andrz:brace-expansion:1.0.0'
}
```

### Manual

[Download JAR from Maven Central][download].

## References

* https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html
* http://wiki.bash-hackers.org/syntax/expansion/brace
* https://github.com/antlr/grammars-v4

## Alternatives

* https://gist.github.com/AndersDJohnson/c81a62266457bb307e29

[brace expansion]: https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html
[BraceExpansion.g4]: src/main/antlr/me/andrz/brace/antlr/BraceExpansion.g4
[antlr]: http://www.antlr.org/
[download]: https://repo1.maven.org/maven2/me/andrz/brace-expansion/1.0.0/brace-expansion-1.0.0.jar
