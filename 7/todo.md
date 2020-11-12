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

## Functionality per Data Representation

###`GameBoard`

[X] need to rework getViablePaths function to make it more readable.

###`GameState`

[X] need to change our GameState to not ever mutate the current GameState and only return a new 
GameState after a method call.

###`GameTree`

[X] need to fix applyFunction method to make it work without creating a new instance of a GameTree 
that refers to the output X of the function.

###`Strategy`

[X] change the purpose statement to specify side effects (For placing a penguin you return an 
Action. the Position of the tile where the player wants to move the penguin is not clear with 
current statement).

[X] change the choosing turn action purpose statement to specify what happens when the current 
player does not have valid moves.

[X] need a unit test for a failed placement for our placement strategy.
  
###`Referee`

[X] need to include a public method for running a single movement turn in the Referee class.

[X] need to include a public method for running a single movement round in the Referee class.

[X] need to include a public method for running a single placement turn in the Referee class.

## Integration Tests

[ ] Check Integration Tests for Milestone 3

[ ] Check Integration Tests for Milestone 4

[ ] Check Integration Tests for Milestone 5

[ ] Check Integration Tests for Milestone 6
