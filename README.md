# Aho-Corasick Bonus Task


## Implementation

- `AhoCor.java`: builds the trie, computes failure and output links.
- `KMP.java`: searches the text separately for each pattern.
- `Match.java`: stores the matched pattern ID
- `Main.java`: runs tests and benchmarks.
- `results.csv`: measured average execution times.

## Tests

Correctness tests:

| Patterns | Text | Expected matches |
|---|---|---:|
| `he`, `she`, `his`, `hers` | `ahishers` | 4 |
| `a`, `aa`, `aaa` | `aaaa` | 9 |
| `abc`, `abc`, `bc` | `zabc` | 3 |
| `abc`, `xyz` | `dddd` | 0 |



The benchmarks use 10, 100, or 1,000 patterns and text lengths of 1,000, 10,000, or 100,000 characters.


## Experimental results

Times are averages in milliseconds;

| Patterns | Text length | Aho-Corasick (ms) | KMP (ms) | Matches |
|---:|---:|---:|---:|---:|
| 10 | 1,000 | 0.307 | 0.478 | 10 |
| 10 | 10,000 | 0.688 | 2.614 | 10 |
| 10 | 100,000 | 2.828 | 3.884 | 10 |
| 100 | 1,000 | 0.314 | 0.279 | 100 |
| 100 | 10,000 | 0.323 | 3.095 | 100 |
| 100 | 100,000 | 2.477 | 34.551 | 101 |
| 1,000 | 1,000 | 3.939 | 3.674 | 1,000 |
| 1,000 | 10,000 | 1.961 | 35.093 | 1,000 |
| 1,000 | 100,000 | 3.960 | 346.098 | 1,000 |

## Complexity analysis

Let `S` be the total length of all patterns, `T` the text length, `P` the number of patterns and `Z` the number of reported matches.

**Aho-Corasick:** Building the trie takes `O(S)` time. Building failure links and completed transitions using BFS takes `O(26*S)` time. Searching takes `O(T+Z)` time. Total time is `O(S+T+Z)` because 26 is constant. Space is `O(26*S+Z)`, including the returned matches or `O(S+Z)` for the fixed alphabet.

The trie shares common prefixes among patterns. Failure links tell the algorithm where to continue after a mismatch instead of restarting from the beginning. Output links lead to other patterns that end at the same text position, including suffix matches. This lets Aho-Corasick find multiple patterns with one pass.

**KMP:** One pattern of length `m` need `O(m+T)` time and `O(m)` extra space for its prefix function. For all `P` patterns total time is `O(S+P*T+Z)`. When processing patterns one at a time, the extra working space is `O(max pattern length)` plus `O(Z)` for stored matches.

## Discussion

Aho-Corasick was generally faster as the number of patterns and text length increased. For 1,000 patterns and a text of 100,000 characters, its average was 3.960 ms, compared with 346.098 ms for KMP. This is because Aho-Corasick scans the text once, KMP scans it for every pattern.

KMP can be better when there are few patterns or a short text. For 100 patterns and 1,000 characters, KMP took 0.279 ms vs 0.314 ms for Aho-Corasick. KMP is simpler for searching a single pattern and uses less memory. Aho-Corasick requires building and storing a trie and failure links, but it is better suited to searching many patterns.
