##  games

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Representing Referees

`Date:` 10/26/2020

A Referee should be able to do the following in order to interact properly with our API:

- Get a copy of the current Game State when called on by a Player

- Gets the player's turn in the current Game State when called on by a Player

- Apply an Action to the current Game State when called on by a Player



**External Interface**

An external interface to help represent our data model for our Referee:

`getGameState()`
- The signature of this method should be `() -> GameState`
    - GameState is the current Game State
- The returned Game State is a copy that can be used to check future moves,
 their score or render the game.

`getPlayerTurn()`
- The signature of this method should be `() -> Whole Number`
    - Whole Number represents the player's place in line to make a move
        - Returns 0 if it is this player's turn or the number of moves until their turn
    
`applyAction()`
- The signature of this method should be `Action -> Maybe GameState`
    - Action is one of:
        - MovePenguin
        - PlacePenguin
    - Maybe GameState is one of:
        - GameState: a GameState with the Action applied
        - Error: the action isn't valid
- If the function returns an error, the player should be removed from the game for cheating.


