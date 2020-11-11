##  manager-protocol

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Manager Protocol

`Date:` 11/2/2020

Externally, a tournament manager should allow: 
- Players to sign up to a tournament 
- Referees to report the results of a game and the current ongoing actions of a game
- Observers to get those current ongoing actions
- Players and Observers to get the results of a round, the results of the entire tournament, and 
the tournament statistics.

Players should be allowed to sign up for a tournament during a signup period. When the signup period ends the 
tournament starts. Once the tournament has started, no more players will be allowed to join.

Referees should be allowed to report game results once a game has concluded and report ongoing actions
as a game is in progress or has just ended.

All other external methods should be available after the sign up phase.

Internally, a tournament manager should divide the players into groups of 2-4 and assign them to 
referees. These referees will run the games and determine the winners. Once all games for a round 
are complete, the results of the games will determine which players move onto the next round and
will continue until the tournament is over.

**External Interface**

`signUp()`
- Player calls this method on Tournament Manager
- The signature of this method is `Player -> Boolean`
    - Player must have a unique identifier and a positive integer as their age
    - Returns true if the player is signed up, otherwise false
- `Purpose:` This method would allow any number of Players to sign up to the tournament in 
a specified time frame.
- The tournament manager should have a way to keep track of every player that has signed 
up in this time frame.
- After the signup period ends, the tournament automatically starts.

`reportGameResult()`
- Referee calls this method on Tournament Manager
- The signature of this method is `GameResult -> ()`
    - A Result represents the results of a single Fish game which should include the placements of 
    all players and a list of the players that attempted to cheat in the game.
- `Purpose:` Allows the referee to report game results including placements and cheating players
- This method should be allowed only after a game has concluded.

`reportOngoingAction()`
- Referee calls this method on Tournament Manager
- The signature of this method is `GameAction -> ()`
    - A GameAction can be one of the following:
        - A Player placing a penguin
        - A Player making a move
        - A Player forced to pass their turn
        - A Player was caught cheating
        - A game has ended
        - Other game information deemed important
- `Purpose:` Allows a Referee to report an ongoing GameAction to the Tournament Manager.
- This method should be allowed only while a game is in progress or just ended.

`getOngoingActions()`
- Observer calls this method on Tournament Manager
- The signature of this method is `() -> List of GameAction`
    - List of GameAction: A list of the most recent GameAction for all current games in 
    the tournament
- `Purpose:` Allows an Observer to see the most recent ongoing GameActions in all games.
- This method should be allowed only after the tournament has started.

`getTournamentResults()`
- Player or Observer calls this method on Tournament Manager
- The signature of this method is `() -> Maybe List of Player`
    - A Maybe List of Player is one of:
        - List of Player: A list of players in the order in which they placed (descending) where the
         winner of the tournament is at index 0. Cheating players are not included in the results.
        - Error: Thrown if tournament is not over or has not started
- `Purpose:` Allows an observer to see the placements of all players of a given tournament.

`getRoundResults()`
- Player or Observer calls this method on Tournament Manger
- The signature of this method is `Number -> Maybe List of GameResult`
    - Number: Positive Integer representing a round in the tournament
    - A Maybe List of GameResult is one of:
        - List of GameResult: A list of GameResults for games in the given round
        - Error: Thrown if the given round hasn't concluded or started
- `Purpose:` Allows an observer to see the results of a given tournament round.
- This method should be allowed only after the tournament has started.

`getTournamentStatistics()`
- Player or Observer calls this method on Tournament Manger
- The signature of this method is `() -> String`
    - A Statistic String represents the current statistics for all players in the tournament
    - A Statistic String can include the following:
        - Player's current or final placement (depending on whether the tournament is ongoing or over)
        - Player's number of wins and losses
        - If a Player cheated
        - Other Player information deemed important
- `Purpose:` Allows an observer to see the current statistics of the tournament.
- This method should be allowed only after the tournament has started.
