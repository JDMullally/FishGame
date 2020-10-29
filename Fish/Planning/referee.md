##  games

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Representing Referees

`Date:` 10/26/2020

From Fish.Com, a Plan, a Referee should be able to do the following.
- A referee supervises an individual game after being handed a number of players
- The referee sets up a board and interacts with the players according to the interface protocol.
- It removes a player—really its penguins—that fails or cheats.
- When the game is over, it reports the outcome of the game and the failing and cheating players
- during the game it may need to inform game observers of on-going actions.

Internally, a Referee must be able to create a GameState by creating a 
GameBoard with a given number of Players.

The creation a of a new Referee must create a custom GameBoard and create a GameState 
with only those given players.  The dimensions and properties of the created GameBoard is up to the
Referee to decide.

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


