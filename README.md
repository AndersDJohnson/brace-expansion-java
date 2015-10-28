# brace-expansion
[Brace expansion] from Bash/shell in Java.

```
a{b,c{0,1}}d

// list
a b d
a c 0 d
a c 1 d

// tree
a
b c
  0 1
d d d
```

## References

* https://github.com/antlr/grammars-v4
* http://wiki.bash-hackers.org/syntax/expansion/brace
* https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html

## Alternatives

* https://gist.github.com/AndersDJohnson/c81a62266457bb307e29

[brace expansion]: https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html
