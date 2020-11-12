#  reworked

`To:` Dot Game CEOs

`From:` Joseph Mullally and Eddie Zhou

`Subject:` Reworked Feedback

`Date:` 11/11/20

## Rework List

#### need private fields final in all structures to prevent mutation errors
we changed penguinList in Player to final
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/2b734a5398d0460ece8b9bab6b2a70341b8128d5>

state field in the GameTree was changed to final
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/079c64c84d2ec76ff629f6884b49cc5656af2fec>

Tile fields changed to final
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/85272dd2bbfe6826795a03e00b8ab5864b0e5d71>

Referee fields changed to final
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/7339644dde0cde6320b9a737a482572a3795584a>


#### need to switch row/column of our GameBoard representation
We changed our formula for converting a 2-D Array of tiles from `Board[x][y] -> Point(x,y)`
to `Board[y][x] -> Point(x,y)` in every instance it existed to properly match the Json Array 
ordering.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/ce4476af5850f37424db9deefbfb57289035ad85>

#### need a clear representation of player-stuck-state
We added a method called `isCurrentPlayerStuck` for this purpose during assigment 5, but we never 
properly implemented it.  We added it to our GameTree `getSubstates` method to check if a player was
stuck in subsequent states.
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/e323a040e3f71de7fa84600febd75b42cda82035>

We then changed our Strategy to check if the player was stuck as opposed to an empty list of 
substates to help with readability.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/846c3888e66be846a28cd34944ae71a2354cb731>

#### need to rework getViablePaths function to make it more readable.
We changed our enums to implement a `Function<Point,Point>` and gave it an apply method that takes 
the current Point and returns the next possible point based on the current Direction Enum.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/0fab3c5a48036ef69fa35c1f302f3b4b6421bc1f>

We were then able to use our existing `validTile` method and a new helped method to reduce our new 
implementation of both helpers to 18 lines of code for our `getViablePaths`.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/2c7a953fa5d754ede26ea728d20725448c63e163>

#### need to change our GameState to not ever mutate.
We had one instance of mutation in our GameState class in our `removePlayer` method.  We would just 
mutate the gameState and had our `PlayerCheated` Action handle the creation of a new GameState. We 
changed our `removePlayer` method to return a new GameState after this occurs.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/6921b688576dccaa64da3b9fa08b6ca1469d0600>

#### need to rework applyFunction to make it usable.
We had to change our GameTree class to not have a built in Generic and added this generic to the
`applyFunction` method. We then fixed all instances of a GameTree to no longer be constructed with 
a Generic type `GameTree<?> -> GameTree`.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/eb1ce9e3933b7ece5073226f37487db7b90a17db>

#### change the purpose statement to clarify an Action in placing and moving penguins
We added a sentence in the purpose statement that defines an Action and how to get positions from 
a given action. We added this in both `choosePlacePenguinAction` and `chooseMoveAction` the 
IStrategy Interface.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/8518fac9469bc2770757dedbd1ec1368967f804b>

#### need a unit test for a failed placement for our placement strategy
We added a unit test that would check what would happen if a Player attempted to place a Penguin on
a board entirely filled with holes to assure that would throw an Illegal Argument Exception.

<https://github.ccs.neu.edu/CS4500-F20/italy/commit/dd7ac9cf1dc3172a66c4b2fd4d17a5a5d470df68>

#### need to include a public method for running a single movement turn in the Referee class.
We decided to split our `runGame` method into separate methods:
- `createInitialGame` which creates an initial GameState which we recently made public 
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/740e7f7dfbff633b2134cd0f347bb18c5713e4e2>
- `placementTurn`
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/ebef750e4ee91aa5fa865835553af528fafde9a0>
- `runTurn` which runs a movement turn
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/5e966ea0cd4263793d14bf013371713efa5532c2>
- `runRound` which runs a movement round.
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/0ff31d2779b4caea9daa9354d0e185bd6d579d06>
