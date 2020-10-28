##  games

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Representing Referees

`Date:` 10/26/2020

The Fish.Com plan for a referee goes as follows.

A referee supervises an individual game after being handed a number of players.

The referee sets up a board and interacts with the players according to the interface protocol.

- It removes a player—really its penguins—that fails or cheats.

- When the game is over, it reports the outcome of the game and the failing and cheating player;

- during the game it may need to inform game observers of on-going actions, preferably with a State.

State is
     
      { "players" : Player*,

        "board" : Board }

To do these things, we need our referee to be able to remove a cheating player and their penguins,
and save that player who cheated and report them.  The referee must also be able to determine when 
the game is over, so they would most like keep track of the game states.  If the referee keeps track
of the game states, they will be able to inform observers of the current game state as our GameState
can render itself.

**External Interface**

An external interface to help represent our data model for our referee:

`runGame();`

`removePlayer();`
- The signature of this method should be `Color -> GameState`
    - Color is the color of the cheating player
    - GameState is a new GameState with that player's penguins removed.
- If an Illegal move is made, the referee is able to remove a player by their color.
- This action should only remove that player's penguins from the game.

`reportGameOver();`: 
- The signature of this method should be `GameState -> Json Element`
    - GameState is the current Game State
    - Json Element is one of:
        - Boolean that indicates that the game is not over
        - Json Object should have a field with the winner and a field with the cheaters.
            - The winner will be represented by a Player.
            - The cheaters will be represented with a Json Array of Player.

`getRenderableGameState();` :
- The signature of this method should be `GameState -> Json Object`
    - GameState is the current Game State
    - Json Object is a State (defined above)
- This State allows observers to render the state.

