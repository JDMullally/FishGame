##  player-protocol

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Player Protocol

`Date:` 10/20/2020

A player should be able to do the following in order to interact properly with our API:

- Place a penguin on the board during placement rounds when called on by the referee.

- Make a move on the board after placement rounds when called on by the referee.

We narrowed this down to two methods that should return Actions.

An Action is one of:
- MovePenguin
- PlacePenguin

`placePenguin()`
- The signature of this method is `GameState -> Action`
    - GameState is the current Game State
    - The Action should be a PlacePenguin Action
- This should only occur in the initial phase of the game. If this occurs after the placement rounds,
the player should be removed.
- If a player places the penguin in an invalid position, in a hole or on another penguin, they 
will be removed from the game.
- A player will be given 10 seconds to place a penguin.  If they time out, they will be removed
 from the game.


`movePenguin()`
- The signature of this method is `(GameState, Natural Number) -> Action`
    - GameState is the current Game State
    - Natural Number is the number of  turns the player should look forward.
    - The Action should be a Move Action
- This cannot occur in the initial phase of the game.  If it is attempted during placement rounds,
 the player should be removed.
 - If a player attempts to move to an invalid position, in a hole or on another penguin, they 
 will be removed from the game.
 - A player will be given 30 seconds to move a penguin.  If they time out, they will be removed
  from the game.
