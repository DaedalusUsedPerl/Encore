# Encore RoR backend

GET '/lobbies' — Returns a JSON list of all lobbies.  
  Example: `http GET localhost:3000/lobbies/` returns
  ```
  [
    {
        "id": 298486374, 
        "name": "Lobby2", 
        "owner_id": 902541635
    }, 
    {
        "id": 980190962, 
        "name": "Lobby1", 
        "owner_id": 1023357144
    }
  ]
 ```
 
POST '/lobbies' — Creates a lobby and returns the JSON object of the lobby  
  Example: `http POST localhost:3000/lobbies name=Hi owner_id=902541635` returns
  ```
  {
    "id": 980190966, 
    "name": "Hi", 
    "owner_id": 902541635
  }
  ```

GET '/lobbies/:id' — Return the JSON object (including QueuedSongs) of the lobby with the given id  
  Example: `http GET localhost:3000/lobbies/298486374` returns
  ```
  {
    "id": 298486374, 
    "name": "Lobby2", 
    "owner_id": 902541635, 
    "queued_songs": [
        {
            "id": 980190964, 
            "position": 2, 
            "song": "Blank Space", 
            "vote_count": 3
        }
    ]
  }
  ```
