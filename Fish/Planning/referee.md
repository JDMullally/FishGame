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

- during the game it may need to inform game observers of on-going actions.

To do these things, we need our referee to be able to remove a cheating player and their penguins,
and save that player who cheated and report them.  The referee must also be able to determine when 
the game is over, so they would most like keep track of the game states.  If the referee keeps track
of the game states, they will be able to inform observers of the current game state as our GameState
can render itself.

**External Interface**

An external interface to help represent our data model for our referee:

`removePlayer(Color cheater);` : If an Illegal move is made, the referee is able to remove a player
 by their color.  This action should only remove that player's penguins from the game.

`reportGameOver();`: Should check if the most recent GameState is over and 
report the winner and cheaters to STDOUT. 

`getRenderableGameState();` : Sends the current GameState to STDOUT as a Json Object to be rendered. 


These three methods used in tandem would allow a Player or Referee to see moves in advance and build
out a data structure to represent full games (such as using a list or a tree).
