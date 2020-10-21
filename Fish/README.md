# Fish

Fish is a board game for two to four players.

### Example
Go to the `Fish` directory and run the following command for information on how to properly run the
executable:

```
$  ./xfish -h
```

The arguments `-r` and `-c` are required arguments representing the number of rows and columns of
the game board. All other arguments are optional.

### Testing
To run the testing harness for milestone 4, go to the `Fish/4` directory and run the following, passing a State to STDIN:

```
$  ./xstate
```

To run the testing harness for milestone 3, go to the `Fish/3` directory and run the following, passing a Board-Posn to STDIN:

```
$  ./xboard
```

To run unit tests, go to the `Fish/Common` directory and run:

```
$  ./test.sh
```