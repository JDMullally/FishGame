## Self-Evaluation Form for Milestone 6

Indicate below where your TAs can find the following elements in your strategy and/or player-interface modules:

The implementation of the "steady state" phase of a board game
typically calls for several different pieces: playing a *complete
game*, the *start up* phase, playing one *round* of the game, playing a *turn*, 
each with different demands. The design recipe from the prerequisite courses call
for at least three pieces of functionality implemented as separate
functions or methods:

- the functionality for "place all penguins"

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/games/Referee.java#L124-L150>**

- a unit test for the "place all penguins" funtionality 

**Note: our function 'runGame' calls the "place all penguins" function. Based on Matthias' answers on piazza this should be acceptable**

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/RefereeTest.java#L102-L115>**

- the "loop till final game state"  function

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/games/Referee.java#L184-L214>**

- this function must initialize the game tree for the players that survived the start-up phase

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/games/Referee.java#L124-L150>**

- a unit test for the "loop till final game state"  function

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/RefereeTest.java#L129-L139>**

- the "one-round loop" function

**We did not break out this functionality into its own function**

- a unit test for the "one-round loop" function

**Note: our function 'runGame' tests this functionality, it just isn't tested directly since we don't have a function for it**

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/RefereeTest.java#L102-L115>**

- the "one-turn" per player function

**We did not break out this functionality into its own function**

- a unit test for the "one-turn per player" function with a well-behaved player 

**Note: our function 'runGame' tests this functionality, it just isn't tested directly since we don't have a function for it**

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/RefereeTest.java#L102-L115>**

- a unit test for the "one-turn" function with a cheating player

**Note: our function 'runGame' tests this functionality, it just isn't tested directly since we don't have a function for it**

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/test/model/RefereeTest.java#L141-L159>**

- a unit test for the "one-turn" function with an failing player 

We were planning to "leave for the phase that adds in remote communication.", so we don't have a 
test with a player that fails to return a move.

**<>**

- for documenting which abnormal conditions the referee addresses 

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/games/IReferee.java#L7-L29>**

- the place where the referee re-initializes the game tree when a player is kicked out for cheating and/or failing 

**<https://github.ccs.neu.edu/CS4500-F20/texline/blob/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish/Common/src/model/games/Referee.java#L152-L164>**


**Please use GitHub perma-links to the range of lines in specific
file or a collection of files for each of the above bullet points.**

  WARNING: all perma-links must point to your commit "6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883".
  Any bad links will be penalized.
  Here is an example link:
    <https://github.ccs.neu.edu/CS4500-F20/texline/tree/6a3fc375d4fe3d8a8bb3dcc7c1971bc6bc8b8883/Fish>

