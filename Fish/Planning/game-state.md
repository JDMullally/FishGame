##  game-state

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Game State Design

`Date:` 9/23/2020

Our representation of the game state for a Fish Game is a 2D-Array of Tile objects and a List of
Penguin objects. The 2D-Array holds information about each individual Tile on the entire game board.
More specifically, the Tile describes its location, how many fish it has, and how to draw itself.
The List of Penguins hold information about each penguin in the game. More specifically, the Penguin
describes its location, which player it belongs to, and how many points it has scored.

An external interface for our game would contain multiple functions to allow players and referees
to interact with and read from the game. Our external interface consists of the following functions
which are described in more detail below: *initialize*, *placePenguin*, *getGameState*,
 *getPossibleMoves*, *makeMove*, and *isGameOver*.

**Initialize:** Allows the referee to specify information regarding the game board including the
number of rows and columns, the location of holes on the board, the minimum number of one fish
tiles, and if all tiles should have the same number of fish. This function returns the initial
game state if the board can be constructed with the provided arguments.

**placePenguin:** Allows players to place a penguin on a specific game board tile. The player will 
specify which penguin they want to place and where they want to place it. This function returns
an updated game state if the penguin can be placed.

**getGameState:** This function returns the current game state as a 2D-Array of Tile and a List of
Penguin.

**getPossibleMoves:** This function returns all moves a given player can make from all of
their penguins. The return type is a map of each penguin to a list of possible moves.

**makeMove:** Allows a player to specify a penguin and location they wish to move the penguin. This
function returns an updated game state if the move is valid and returns an error message if the move
is invalid.

**isGameOver:** Returns true if the game is over (no player can make a move) and false otherwise.