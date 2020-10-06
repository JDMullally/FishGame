###  game-state
To: Dot Game CEOs

From: Joseph Mullally and Mark Preschern

Subject: Milestone Identification

Date: 9/23/20

Our implementation of the game state is our board that holds information about its contents.
It is a two-dimensional array that contains Tiles that know their own location, how 
many fish they have and how to draw themselves.

An external interface for our game state would have a method that allows players to make moves on 
the current game-state.  It would accept a penguin, a player and a move and send that move to the
referee to check.
 
It would also have rule checking method for our referee, that would pass in a move
made by a player and that player. This method will determine whether that move was valid 
depending on the rules of the game.
 
Another member of our interface would be a method that takes in the game-state and determines
whether the game is over.  The referee will keep track of each player's turn and call this if the
game is over.  This method should return the winner of the game.
 
Our external interface would also need to have some sort of initialize game
method that would allow the referee to start the game and have players place penguins.  It should 
take in a list of players and arguments to start the game and determine which player places their 
penguins first.

We would also want a method that could check the current game state and gives the current board and
the player who is currently taking their turn.  This method could be called by any player and should
require no inputs aside from the game state.

We would need a method that takes in the game state and gives a list of the current players
 to the referee.

