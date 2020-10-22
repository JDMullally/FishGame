##  player-protocol

`To:` Dot Game CEOs

`From:` Joseph Mullally and Mark Preschern

`Subject:` Player Protocol

`Date:` 10/20/2020

A player should be able to do the following in order to interact properly with our API:

- Get All Current Games
- Get a Game (GameState) by ID 
- Get a Game's GameTree
- Get if a Game is over
- Get All Players in a Game
- Get a Player in a Game by ID
- Get a Player's turn in a Game
- Get a Player's score in a Game
- Get all of a Player's Penguins in a Game
- Get all of a Player's possible moves with a Penguins in a Game
- add a new Player in a Game
- add a Penguin for a Player in a Game
- move a Penguin for a Player in a Game

This implies the a User has a one to many relationship between them and Games, a Game has a one to
many relationship with players, and a player has a one to many relationship with their penguins.

We decided that best protocol for our API would be a RESTFUL one.

The following is an example of the request methods for a player to use.

#### Request Methods

To Get All Current Games:

`GET /games`

To Get a Game (GameState) by ID: 

`GET /games/{id}`

To Get a Game's GameTree:

`GET /games/{id}/tree`

To Get if a Game is over:

`GET /games/{id}/gameover`

To Get All Players in a Game:

`GET /games/{id}/players`

To Get a Player in a Game by ID:

`GET /games/{id}/players/{id}`

To Get a Player's turn in a Game:

`GET /games/{id}/players/{id}/turn`

To Get a Player's score in a Game:

`GET /games/{id}/players/{id}/score`

To Get all of a Player's Penguins in a Game:

`GET /games/{id}/players/{id}/penguins`

To Get all of a Player's possible moves with a Penguins in a Game:

`GET /games/{id}/players/{id}/penguins/{id}/moves`

To add a new Player in a Game:

`POST /games/{id}/players`

To add a Penguin for a Player in a Game:

`POST /games/{id}/players/{id}/penguins/{id}`

To move a Penguin for a Player in a Game:

`POST /games/{id}/players/{id}/penguins/{id}/move`
