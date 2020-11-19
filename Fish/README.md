# Fish

Fish is a board game for two to four players.

###File Structure
`/Admin` : Contains references to the interface for the tournament manager, 
and implementation of the Referee.

`/Planning` : Contains all self evaluations along with all memos to Dot Game CEOs for our design 
plans regarding the interface and protocol of the game, referee, players, tournament manager, 
project milestones and system requirements.

`/Player` : Contains references to the implementation of our strategy and In-house player AI.

`/Common` : Contains the implementations of our milestones.  All references in other files point to 
files in `/src` within this folder.  This folder also contains all JUnit tests used by our xtest 
script within `/test` and our Makefile for building our project.  Here you can also find references 
to our implementation of a game-tree, player-interface, and state. 

### Example
Go to the `Fish` directory and run the following command for information on how to properly run the
executable:

```
$  ./xfish -h
```

The arguments `-r` and `-c` are required arguments representing the number of rows and columns of
the game board. All other arguments are optional.

### Refactoring
To see all changes made in milestone 7, go to the `../7` and see:
* `todo.md`
* `bugs.md`
* `reworked.md`

### Testing
To run unit tests, go to the `Fish` directory (this directory) and run

```
$  ./xtest
```

To run the testing harness for milestone 8, go to the `../8` directory and run the following, passing a Game-Description to STDIN:

```
$ ./xref
```

To run the testing harness for milestone 6, go to the `../6` directory and run the following, passing a Depth-State to STDIN:

```
$ ./xstrategy
```

To run the testing harness for milestone 5, go to the `../5` directory and run the following, passing a Move-Response-Query to STDIN:

```
$ ./xtree
```

To run the testing harness for milestone 4, go to the `../4` directory and run the following, passing a State to STDIN:

```
$  ./xstate
```

To run the testing harness for milestone 3, go to the `../3` directory and run the following, passing a Board-Posn to STDIN:

```
$  ./xboard
```
