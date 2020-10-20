##  player-protocol

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Player Protocol

`Date:` 10/20/2020

A player should be able to see all possible games, get a specific game by its ID,
create a player in a game, get all the players in a current game,
get a specific player from a specific game, look at their own Penguins, get a specific Penguin,
get all possible moves with a specific penguin and make a move with their penguin.

This implies the a User has a one to many relationship between them and Games, 
a Game has a one(two) to many relationship with players, a player has a one to many
relationship with their penguins and penguin has a one to many relationship with it's moves.

We decided that best protocol for our API would be a RESTFUL one.

The following is an example of the request methods for a player.

#### Request Methods

To Get All Current Games:

`GET /games`

To Get a specific Game by ID: 

`GET /games/{id}`

To Create a new Player:

`POST /games/{id}/players`

To Get All Players in a specific Game:

`GET /games/{id}/players`

To Get a specific Player in a specific game by ID:

`GET /games/{id}/players/{id}`

To Get a specific Player's Penguins:

`GET /games/{id}/players/{id}/penguins`

To Get a Penguin by ID:

`GET /games/{id}/players/{id}/penguins/{id}`

To Get all moves with a specific Penguin

`GET /games/{id}/players/{id}/penguins/{id}/moves`

To Make a move

`PUT /games/{id}/players/{id}/penguins/{id}/moves/{id}`

####Request Protocols

