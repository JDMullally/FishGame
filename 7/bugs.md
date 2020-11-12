#  bugs

`To:` Dot Game CEOs

`From:` Joseph Mullally and Eddie Zhou

`Subject:` Bug Feedback

`Date:` 11/11/20


## Bug List
#### we changed our Strategy to check if the player was stuck to prevent an infinite loop.
After changing our GameTree to keep track of stuck players, we encountered an infinite loop in our
`Strategy`.  We needed to change our check for empty substates to instead check if the current node 
is stuck with `isCurrentPlayerStuck`.
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/846c3888e66be846a28cd34944ae71a2354cb731>

#### needed to fix deprecated unit tests for removing a player that didn't properly compare GameStates.
After we changed our `GameState`'s `removePlayer` function to return a new GameState, we had to 
change our unit tests that compared the players we were initially returning.
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/a24b980f84673192f33b92f3165505cacd2538d1>

#### we changed our Referee's Data representation to use a linked HashMap to maintain order and assure that our players started in the correct order.
We found a bug in our `initializePlayerInterfaces` method in our `Referee` class that initialized 
our players out of order. To fix this, we needed to use a LinkedHashMap instead of a HashMap so that
we maintain the order we add them in our for loop.
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/7031270b5b39ea681f64e15cdb69b0655e5057b2>

#### we needed our referee to accounted for timeouts or a player could stall the entire game.
We utilized executors and the Callable class to wait for a move for a given period of time.
<https://github.ccs.neu.edu/CS4500-F20/italy/commit/0ff31d2779b4caea9daa9354d0e185bd6d579d06>

#### we needed our isGameOver method to check if the game is ready before it returns true or false
Our original implementation of `isGameOver` did not check if the the placement rounds were over, so
it returned true when no Penguins have been placed.  We added a check at the top the would determine
if the game was ready to assure that `isGameOver` only returned false in a valid game where all 
penguins have already been placed.

> #### we needed our isGameOver method to check if all the players cheated before we return false if the players cheated
> We had another bug resulting from this change that had multiple tests failing because `isGameReady` 
> makes its decision based on the number of players, but if all players were caught cheating their
> penguins would be missing and send true which would result in the Game not being over even if it 
> was. We fixed this by adding a check to determine if the game had no more players.
>

#### we need GameState to validate that the potential game is playable
We needed to check if a generated GameBoard is too small for the number of players given, so we 
added a condition in validateInitialPlayers that checks if the GameBoard is too small for the given
number of players that is only checked if this is the first time the method is called 
(or if no penguins are place on the board).

