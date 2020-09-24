# E - TCP

xtcp, a program that consumes a sequence of well-formed JSON values from the input side of a TCP connection and delivers JSON to the output side of a TCP connection after the input side is closed.

### Example

```
$  ./xtcp positive-integer
```

The program’s takes at most one command-line argument. This argument specifies the port to which the program connects (on the default network card). If there is no command-line argument, the program uses the default port 4567. Also, the program waits for at most 3 seconds for a client to connect; if no client connects, the program terminates with an error.
