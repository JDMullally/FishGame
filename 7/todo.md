#  todo

`To:` Dot Game CEOs

`From:` Joseph Mullally and Eddie Zhou

`Subject:` Todo WorkList

`Date:` 11/11/20

## Data Representation

###`Overall`

[X] need to make private fields final in all structures to prevent mutation errors in 
the future.

###`GameBoard`

[X] need to switch row/column of our GameBoard representation for the 2-D Array of Tiles and all 
method functionality to make integration tests work without use of invertBoard method.

###`GameTree`

[X] need a clear representation of isPlayerStuck in our GameTree for creating sub states for our 
tree.

### `Referee`

[ ] need to change Referee to use GameTrees rather than GameStates.

## Functionality per Data Representation

###`GameBoard`

[X] need to rework getViablePaths function to make it more readable.

###`GameState`

[X] need to change our GameState to not ever mutate the current GameState and only return a new 
GameState after a method call.

[X] we needed our isGameOver method to check if the game is ready before it returns true or false

[X] we need GameState to validate that the potential game is playable (a board with 8 non-empty 
tiles can't run a game for three players)

###`GameTree`

[X] need to fix applyFunction method to make it work without creating a new instance of a GameTree 
that refers to the output X of the function.

###`Strategy`

[X] change the purpose statement to clarify an Action in the context of placing a Penguin. The
Position of the tile where the player wants to move the penguin is not clear with current 
statement).

[X] change the choosing turn action purpose statement to specify what happens when the current 
player does not have valid moves.

> *Note: the above two are completed in a single rework labelled "change the purpose statement to 
clarify an Action in placing and moving penguins" in reworked.md*

[X] need a unit test for a failed placement for our placement strategy.
 
###`Referee`

[X] need to include a public method for running a single movement turn in the Referee class.

[X] need to include a public method for running a single movement round in the Referee class.

[X] need to include a public method for running a single placement turn in the Referee class.

> *Note: the above three are completed in a single rework labelled "need to include a public method 
for running a single movement turn in the Referee class." in reworked.md*

[ ] need unit tests for public method for running a single movement turn in the Referee class.

[ ] need unit tests for public method for running a single movement turn in the Referee class.

[ ] need unit tests for public method for running a single movement turn in the Referee class.


## Integration Tests

[ ] Check Integration Tests for Milestone 3

[ ] Check Integration Tests for Milestone 4

[ ] Check Integration Tests for Milestone 5

[ ] Check Integration Tests for Milestone 6
