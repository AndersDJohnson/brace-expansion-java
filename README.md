# BraceExpansion
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

[brace expansion]: https://www.gnu.org/software/bash/manual/html_node/Brace-Expansion.html
