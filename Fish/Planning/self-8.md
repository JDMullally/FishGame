## Self-Evaluation Form for Milestone 8

Indicate below where your TAs can find the following elements in your strategy and/or player-interface modules:

1. did you organize the main function/method for the manager around
the 3 parts of its specifications --- point to the main function
<https://github.ccs.neu.edu/CS4500-F20/italy/blob/c9089586670a43b52849d78088975b8045ae756b/Fish/Common/src/model/tournament/TournamentManager.java#L58-L68>


2. did you factor out a function/method for informing players about
the beginning and the end of the tournament? Does this function catch
players that fail to communicate? --- point to the respective pieces

<https://github.ccs.neu.edu/CS4500-F20/italy/blob/c9089586670a43b52849d78088975b8045ae756b/Fish/Common/src/model/tournament/TournamentManager.java#L91-L105>


3. did you factor out the main loop for running the (possibly 10s of
thousands of) games until the tournament is over? --- point to this
function.

Our main loop for running our games is in our `runTournament` method. `runTournament` also calls our 
private method `informPlayers`, so it is not entirely factored out. However, we did factor out 
running a single round within this loop using the `runRound` method. We also have method to run a 
single game called `runGame` in the tournament which is used by `runRound`.

`runTournament`: 
<https://github.ccs.neu.edu/CS4500-F20/italy/blob/c9089586670a43b52849d78088975b8045ae756b/Fish/Common/src/model/tournament/TournamentManager.java#L58-L68  >

`runRound`: 
<https://github.ccs.neu.edu/CS4500-F20/italy/blob/c9089586670a43b52849d78088975b8045ae756b/Fish/Common/src/model/tournament/TournamentManager.java#L137-L157>

`runGame`:
<https://github.ccs.neu.edu/CS4500-F20/italy/blob/c9089586670a43b52849d78088975b8045ae756b/Fish/Common/src/model/tournament/TournamentManager.java#L159-L201>

**Please use GitHub perma-links to the range of lines in specific
file or a collection of files for each of the above bullet points.**


  WARNING: all perma-links must point to your commit "c9089586670a43b52849d78088975b8045ae756b".
  Any bad links will be penalized.
  Here is an example link:
    <https://github.ccs.neu.edu/CS4500-F20/italy/tree/c9089586670a43b52849d78088975b8045ae756b/Fish>

