# brace-expansion
[Brace expansion] from sh/bash in Java.

```
a{b,c{0,1}}d

// list
a b 0 d
a b 1 d
a c 0 d
a c 1 d

// tree
a
b   c
0 1 0 1
d d d d
```

## References

* https://github.com/antlr/grammars-v4
* http://wiki.bash-hackers.org/syntax/expansion/brace
* https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html

## Alternatives

* https://gist.github.com/AndersDJohnson/c81a62266457bb307e29

[brace expansion]: https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html
