##  games

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Representing Entire Games

`Date:` 10/14/2020

A data representation for representing one entire game can be an ordered list of game states. In
terms of our implementation, that would be a List<GameState>.

A data representation for representing a game's reachable game states is a tree of game states. This
data structure would look the following way:
- If there are moves available from a given GameState, the resulting GameState's of these valid
moves will be children nodes of the current Gamestate in our tree.
- If there are no more moves available from a given GameState, they will be leafs in the tree.

With a data representation being a tree, players and referees can check the validity of certain
moves and plan ahead.

**External Interface**

An external interface to help represent such data structures would contain the following functions:

- getPlayerTurn(): Returns which player's turn it currently is

- getAvailableMoves(): Returns a List of GameState consisting of all available moves.

- move(Player player, Penguin penguin, Point point): Makes a valid move and returns a new GameState.

These three methods used in tandem would allow a Player or Referee to see moves in advance and build
out a data structure to represent full games (such as using a list or a tree).