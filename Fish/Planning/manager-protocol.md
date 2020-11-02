##  manager-protocol

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Manager Protocol

`Date:` 11/2/2020

Externally, a tournament manager should allow: 
- Players to sign up to a tournament 
- Referees to report the results of a game and the current ongoing actions of a game
- Observers to get those current ongoing actions
- Player and Observers get the results of a round, the results of the entire tournament and 
the tournament statistics.

Players should be allowed to sign up for 30 seconds, and then the sign up period ends the 
tournament starts.  Once the tournament has started, no more players will be allowed to join.

All other external methods should be available after the sign up phase.

Internally, a tournament manager should divide the players into groups of 2-4 and assign them to 
referees.  These referees will run the games and determine the winners.  Once all games are complete, 
the winning players are gathered and stored.  The tournament will repeat
this process with each round's winning players until a single player remains.

**External Interface**

`signUp()`
- Player calls this method on Tournament Manager
- The signature of this method is `Player -> Boolean`
    - Player must have a positive integer as their age.
    - Boolean will return true if the player is signed up, otherwise false
- `Purpose:` This method would allow any number of Players to sign up to the tournament in 
a specified time frame
- The tournament manager should have a way to keep track of every player that has signed 
up in this time frame

`ReportGameResult`
- Referee calls this method on Tournament Manager
- The signature of this method is `GameResult -> ()`
    - A Result represents the results of a single Fish game which should include the placements of 
    all players and a list of the players that attempted to cheat in the game.
- `Purpose:` Allows the referee to report cheating players
- This method should be allowed only after the tournament has started.

`ReportOngoingAction`
- Referee calls this method on Tournament Manager
- The signature of this method is `GameAction -> ()`
    - An Action can be one of the following:
        - A Player was caught cheating
        - The game has ended
        - Other important game information
- `Purpose:` Allows a Referee to report a ongoing GameAction to the Tournament Manager.
- This method should be allowed only after the tournament has started.

`getOngoingActions`
- Observer calls this method on Tournament Manager
- The signature of this method is `() -> List of GameAction`
    - List of GameAction: A list of the most recent Game Action for all current games in 
    the tournament
- `Purpose:` Allows an Observer to see the most recent Ongoing Actions in all games.
- This method should be allowed only after the tournament has started.

`getTournamentResults`
- Player or Observer calls this method on Tournament Manager
- The signature of this method is `() -> TournamentResults`
    - A TournamentResult is one of:
        - Player: The player that has won the entire tournament
        - Error: Thrown if tournament is not over or has not started
- `Purpose:` Allows an observer to see the winner of a given tournament
- This method should be allowed only after the tournament has started.

`getRoundResults`
- Player or Observer calls this method on Tournament Manger
- The signature of this method is `Number -> Maybe List of GameResult`
    - Number: Positive Integer representing a round in the tournament
    - A Maybe List of GameResult is one of:
        - List of GameResult: A list of GameResults for games in the given round
        - Error: Thrown if given round hasn't concluded or started
- `Purpose:` Allows an observer to see the results of a given tournament round
- This method should be allowed only after the tournament has started.

`getTournamentStatistics`
- Player or Observer calls this method on Tournament Manger
- The signature of this method is `() -> Statistic`
    - A Statistic represents the current statistics for all players in the tournament
- `Purpose:` Allows an observer to see the current statistics of the tournament
- This method should be allowed only after the tournament has started.
