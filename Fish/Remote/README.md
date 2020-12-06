### File Structure

`server.java` : Contains references to server component interface labelled as Server.java and server 
component implementation labelled as SignUpServer.java that implements the methods from the
server component interface.

`client.java` : Contains references to client component interface labelled as ClientInterface.java 
and Client component implementation labelled as Client.java that implements the methods from the 
client component interface.

### Runnables

To run the server runnable from milestone 9, go to the `../10` directory and run the following 
command

```
$ ./xserver p
```

- p is a natural number that represents the server's port where clients will connect to 


To run the client runnable from milestone 9, go to the `../10` directory and run either of the 
following commands

```
$ ./xclients n p ip 
```

or 

```
xclients n p
```

- n is a number between 1 and 10 that represents the number of clients connecting the tournament
- p is a natural number that represents the port of the server the clients are connecting to
 - ip is the IP of the server they are connecting to. If the ip is omitted, the clients connect to localhost instead

### Modifications

- Set all delays in Tournament and Referee to one second 
<https://github.ccs.neu.edu/CS4500-F20/unionridge/commit/b8bd430e839f9baec257318c54660e0cc1144879>

- Added methods for retrieving players colors and included them in the implementation of the Referee
<https://github.ccs.neu.edu/CS4500-F20/unionridge/commit/de72e51f2418a06265097ce920fd4591039f4b86>

- Changed constructor in GameState that would fail to calculate the number of cheaters in a Game 
and create a an unplayable GameState.
<https://github.ccs.neu.edu/CS4500-F20/unionridge/commit/9b42055de58693421620170739ac57603c9c120e>

- Fixed bug with serialization failing if the first player's move cheats, the other players would 
be given unplayable GameStates
<https://github.ccs.neu.edu/CS4500-F20/unionridge/commit/e207c7d7df51796f9d69ca6150bb3d15e5ddfed6>

- Changed a util class that would convert GameStates to Json representation of States. 
<https://github.ccs.neu.edu/CS4500-F20/unionridge/commit/390763120be6b941d4ca30e3c184db69f4a6f843>


