## Self-Evaluation Form for Milestone 7

Please respond to the following items with

1. the item in your `todo` file that addresses the points below.
    It is possible that you had "perfect" data definitions/interpretations
    (purpose statement, unit tests, etc) and/or responded to feedback in a 
    timely manner. In that case, explain why you didn't have to add this to
    your `todo` list.

2. a link to a git commit (or set of commits) and/or git diffs the resolve
   bugs/implement rewrites: 

These questions are taken from the rubric and represent some of the most
critical elements of the project, though by no means all of them.

(No, not even your sw arch. delivers perfect code.)

### Board

- a data definition and an interpretation for the game _board_

We changed our definition in the below commit and todo item.

Todo item: *need to switch row/column of our GameBoard representation for the 2-D Array of Tiles and 
all method functionality to make integration tests work without use of invertBoard method.*

Commit Link: <https://github.ccs.neu.edu/CS4500-F20/italy/commit/ce4476af5850f37424db9deefbfb57289035ad85>

We then updated our interpretation for the game _board_ to reflect our quality of life changes and 
confirm that we mention the layout of our board and how all tiles are hexagons.

Commit Link: <https://github.ccs.neu.edu/CS4500-F20/italy/commit/2c7a953fa5d754ede26ea728d20725448c63e163>

- a purpose statement for the "reachable tiles" functionality on the board representation

We already had a purpose statement for this for our reachable tile functionality on the board prior 
to this milestone.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/board/IGameBoard.java#L58-L62>

- two unit tests for the "reachable tiles" functionality

We already had unit tests for a "reachable tile" prior to this milestone.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/GameBoardTest.java#L210-L329>

### Game States 


- a data definition and an interpretation for the game _state_

We already fixed our definition and interpretation of game _state_ prior to this milestone.
We received a note in milestone 3 that our interpretation was misleading because we extended
a game board instead of having one.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/state/GameState.java#L17-L23>

- a purpose statement for the "take turn" functionality on states

We didn't change our purpose statements for taking a turn as we knew that a turn could only occur if
the player placed a penguin, moved a penguin, was caught cheating or was forced to pass their turn 
by the referee.  We didn't abstract this purpose statement for taking a turn.

We do however, have a purpose statement from `playerTurn` that specifies that the method returns
the player who is currently first in the turn order.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/state/IGameState.java#L26-L33>

- two unit tests for the "take turn" functionality 

We already had tests for taking turns.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/GameStateTest.java#L276-L408>

### Trees and Strategies


- a data definition including an interpretation for _tree_ that represent entire games

Todo item: *need a clear representation of isPlayerStuck in our GameTree for creating sub states for our tree.*

Commit Link: <https://github.ccs.neu.edu/CS4500-F20/italy/commit/e323a040e3f71de7fa84600febd75b42cda82035>
and <https://github.ccs.neu.edu/CS4500-F20/italy/commit/846c3888e66be846a28cd34944ae71a2354cb731>

- a purpose statement for the "maximin strategy" functionality on trees

We already had a purpose statement for the maximin strategy prior to the refactor.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/strategy/Strategy.java#L24-L28>

- two unit tests for the "maximin" functionality 

We already had these tests before the refactor.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/StrategyTest.java#L177-L313>

### General Issues

Point to at least two of the following three points of remediation: 


- the replacement of `null` for the representation of holes with an actual representation 

We already utilized EmptyTiles to avoid null as a representation for holes.

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/board/EmptyTile.java>

- one name refactoring that replaces a misleading name with a self-explanatory name

We forgot to document this in our todo, but we did refactor replaceTile to removeTile.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/c5dfd1a8b8788b729b46f09c434649f706e2ab45>

- a "debugging session" starting from a failed integration test:
  - the failed integration test
  - its translation into a unit test (or several unit tests)
  - its fix
  - bonus: deriving additional unit tests from the initial ones 


### Bonus

Explain your favorite "debt removal" action via a paragraph with
supporting evidence (i.e. citations to git commit links, todo, `bug.md`
and/or `reworked.md`).

We reworked get viable paths to 18 lines of code from nearly 100. We needed to change our enum 
interpretation implement a function that got the next position based on the given one. We could
then iterate through all our Direction enums and get all possible viable paths in a much cleaner 
way. Below is the todo, reworked.md, and git commit links for our changes.

Todo item: need to rework getViablePaths function to make it more readable.

reworked item: need to rework getViablePaths function to make it more readable.

Git commits: <https://github.ccs.neu.edu/CS4500-F20/italy/commit/0fab3c5a48036ef69fa35c1f302f3b4b6421bc1f> and 
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/2c7a953fa5d754ede26ea728d20725448c63e163>

###Note
We use the link from our last commit from milestone 6 to prove any that implementations we didn't 
change were there before milestone 7.
<https://github.ccs.neu.edu/CS4500-F20/italy/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883>
