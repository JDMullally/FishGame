## Self-Evaluation Form for Milestone 9

You must make an appointment with your grader during his or her office
hour to demo your project. See the end of the self-eval for the assigned
grader. 

Indicate below where your TA can find the following elements in your strategy 
and/or player-interface modules: 

1. for human players, point the TA to
   - the interface (signature) that an AI player implements
   - the interface that the human-GUI component implements
   - the implementation of the player GUI

2. for game observers, point the TA to
   - the `game-observer` interface that observers implement 
   https://github.ccs.neu.edu/CS4500-F20/unionridge/blob/a64abb1e1dcc31773b9617a89bc54eb495768653/Fish/Common/src/model/humans/GameObserver.java#L12
   - the point where the `referee` consumes observers 
   - the callback from `referee` to observers concerning turns

3. for tournament observers, point the TA to
   - the `tournament-observer` interface that observers implement 
   - the point where the `manager` consumes observers 
   - the callback to observes concerning the results of rounds 

Our Game Visualizer consumes a list of observers and updates the observer on every action done by the state concering rounds.

Here is where we consume the list of observers 

https://github.ccs.neu.edu/CS4500-F20/unionridge/blob/a64abb1e1dcc31773b9617a89bc54eb495768653/Fish/Common/src/model/humans/GameVisualizer.java#L30

Here is where we update the observer based on action

https://github.ccs.neu.edu/CS4500-F20/unionridge/blob/a64abb1e1dcc31773b9617a89bc54eb495768653/Fish/Common/src/model/humans/GameVisualizer.java#L69

Do not forget to meet the assigned TA for a demo; see bottom.  If the
TA's office hour overlaps with other obligations, sign up for a 1-1.


**Please use GitHub perma-links to the range of lines in specific
file or a collection of files for each of the above bullet points.**


  WARNING: all perma-links must point to your commit "a64abb1e1dcc31773b9617a89bc54eb495768653".
  Any bad links will be penalized.
  Here is an example link:
    <https://github.ccs.neu.edu/CS4500-F20/unionridge/tree/a64abb1e1dcc31773b9617a89bc54eb495768653/Fish>

Assigned grader = Ritika Gupta (gupta.ritika@northeastern.edu)

