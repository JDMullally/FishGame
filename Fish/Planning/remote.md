##  remote player interaction

`To:` Dot Game CEOs

`From:` Joseph Mullally and Eddie Zhou

`Subject:` Design for Remote Collaboration Protocol

`Date:` 11/17/2020

## Interaction Diagram

[Click to see Diagram](https://static.swimlanes.io/bedb407754938a885f3a109f51c8dc82.png)

or See Planning/Proxy-Protocol-Diagram.png

## Relevant Components

- Remote Client - Potential player in Fish Tournament. Player must sign up to recieve a Player Proxy to communicate with the Game Logic Layer.
- Sign Up Server - A single server that interacts with all remote players at the start to sign them up for a tournament.
- Player Proxy - Is able to translate message between the Remote Client and Game Logic. It implements the `PlayerInterface` from our Game Logic.
- Game Logic - Our existing Tournament Manager, Referee and the common code it uses.

## Protocol Overview

Communication between Remote Clients and the Sign Up Server and  Player Proxy should use TCP for all interactions. All messages are formatted as JSON.


## Protocol Description


### Sign up phase
1. Remote Client sends a request to the Sign Up Server
2. Sign Up Server uses request to create a Player Proxy for the Remote Client. Specifically, a `RemotePlayer` object that implements `PlayerInterface`
3. Sign Up Server sends confirmation to Remote Client to confirm that a Player Proxy was created for them.
4. Remote Client initiates connection to the Player Proxy.
5. This occurs for any additional Remote Clients until a tournament has enough players to begin.

### Tournament Start
6. The Sign Up Server sends all of the Player Proxy Objects (`RemotePlayer` Objects) to the Game Logic to have it construct a Tournament Manager.
7. When the tournament begins, the Game Logic Layer will send a message to the Player Proxy that indicates that the game is starting as a call to the player's `tournamentHasStarted()` method.
8. The Player Proxy will 'forward' this message to the remote Client over TCP as JSON text.
9. Remote Client sends back an acknowledgement as JSON to the Player Proxy. 
10. The Player Proxy will 'return' this message to the Game Logic as a boolean`true` response to `tournamentHasStarted()`. If the Game Logic does not receive a response from the Player Proxy within 5 seconds, the player is removed from the Tournament.

### Game Start
11. When the game begins, the Game Logic Layer will send a message to the Player Proxy that indicates that the game is starting as a call to the player's `gameHasStarted()` method.
12. The Player Proxy will 'forward' this message to the remote Client over TCP as JSON text.
13. Remote Client sends back an acknowledgement as JSON to the Player Proxy. 
14. The Player Proxy will 'return' this message to the Game Logic as a boolean`true` response to `gameHasStarted()`. If the Game Logic does not receive a response from the Player Proxy within 5 seconds, the player is removed from the Game and Tournament.

### Game Loop
15. When it is the Remote Client's turn in the game, the Game Logic Layer will request either a Placement `Action` or Movement `Action` by calling `placePenguin()` or `movePenguin()` method on the Player Proxy.
16. The Player Proxy will 'forward' this request along with the updated game state as JSON to the Remote Client to get the requested JSON representation of the `Action`.
17. The Remote Client sends back their JSON Action to the Player Proxy.
18. The Player Proxy will translate and 'return' this `Action` to Game Logic Layer in response to `placePenguin()` or `movePenguin()`. If the Game Logic Layer does not receive a valid response from the Player Proxy within 30 seconds, the player is removed from the Game and Tournament.

### Game End
19. When the game ends, the Game Logic Layer will send a message to the Player Proxy that indicates that the game has ended as a call to the player's `getGameResults()` method.
20. The Player Proxy will 'forward' this message to the remote Client over TCP as JSON text.
21. Remote Client sends back an acknowledgement as JSON to the Player Proxy. 
22. The Player Proxy will 'return' this message to the Game Logic as a boolean `true` response to `getGameResults()`. If the Game Logic does not receive a response from the Player Proxy within 5 seconds, the player is removed from the Game and Tournament.

### Tournament End
23. When the tournament ends, the Game Logic Layer will send a message to the Player Proxy that indicates that the game has ended as a call to the player's `tournamentResults()` method.
24. The Player Proxy will 'forward' this message to the remote Client over TCP as JSON text.
25. Remote Client sends back an acknowledgement as JSON to the Player Proxy.
26. The Player Proxy will 'return' this message to the Game Logic as a boolean `true` response to `tournamentResults()`. If the Game Logic does not receive a response from the Player Proxy within 5 seconds, the player is removed from the Game and Tournament.
