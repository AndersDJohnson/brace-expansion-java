# brace-expansion
[Brace expansion] from Bash/shell in Java.

```
a{b,c{0..2}}d

// list
a b d
a c 0 d
a c 1 d
a c 2 d

// tree
a
b c
  0 1 2
d d d d
```

This project provides an [ANTLR][] 4 grammar, [BraceExpansion.g4][].

## References

* https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html
* http://wiki.bash-hackers.org/syntax/expansion/brace
* https://github.com/antlr/grammars-v4

## Alternatives

* https://gist.github.com/AndersDJohnson/c81a62266457bb307e29

[brace expansion]: https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html
[BraceExpansion.g4]: src/main/antlr/me/andrz/brace/antlr/BraceExpansion.g4
[antlr]: http://www.antlr.org/
