# Encore RoR backend API

## Installation and Running Instructions
The backend API is deployed at `http://encoreapp.me`
You may directly use this deployed API if you don't want to run the code yourself.

If you want to run the code yourself, follow these instructions:

This tutorial was performed on a fresh copy of [Ubuntu 14.04.3 LTS 64-bit](http://releases.ubuntu.com/14.04.3/ubuntu-14.04.3-desktop-amd64.iso.torrent) running in VirtualBox.

### First you must install Ruby 2.1.7 and Rails 4.2.4.
* `gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3`
* `\curl -sSL https://get.rvm.io | bash -s stable` to install rvm
* `source /home/[your user name]/.rvm/scripts/rvm` or reopen shell window
* `rvm install 2.1.7`
* `rvm requirements`
* `sudo apt-get install libgmp-dev`
* `rvmsudo gem install rails --version 4.2.2`

### Once you have those installed run:
* `sudo apt-get install git`
* `git clone https://github.com/DaedalusUsedPerl/Encore.git`
* `cd Encore/encore_backend`
* `bundle install`
* `sudo apt-get install nodejs`
* `rake db:create db:migrate`
* `rake db:fixtures:load` to load example data into database
* `rails server`
The server will be running on localhost:3000 and you can try out the API using curl or a web browser.
For example, http://localhost:3000/lobbies will return the list of lobbies. See below for full API description.

### To run tests
* `RAILS_ENV=test rake db:create db:migrate`
* `rake test`
Tests are located in the 'test' directory. Specifically, controller tests are in the 'test/controllers' folder and model tests are in the 'test/models` folder.

## Description of API
GET '/lobbies' — Returns a JSON list of all lobbies.  
  Example: `http GET encoreapp.me/lobbies/` returns
  ```
  [
    {
        "id": 298486374, 
        "name": "Lobby2", 
    },
    {
        "id": 980190962, 
        "name": "Lobby1", 
    }
  ]
 ```
 
POST '/lobbies' — Creates a lobby and returns the JSON object of the lobby.
Request must include the name of the lobby and an owner_id corresponding to the unique_id of the creator (generated on Android app, the device id maybe?).
  Example: `http POST encoreapp.me/lobbies name=Hi owner_id=902541635` returns
  ```
  {
    "id": 980190966, 
    "name": "Hi", 
  }
  ```

GET '/lobbies/:id' — Return the JSON object (including QueuedSongs) of the lobby with the given id  
  Example: `http GET encoreapp.me/lobbies/298486374` returns
  ```
  {
    "id": 298486374, 
    "name": "Lobby2", 
    "queued_songs": [
        {
            "id": 980190964, 
            "position": 2, 
            "title": "Blank Space",
            "artist": "Taylor Swift",
            "rdio_id": "1234567",
            "vote_count": 3
        }
    ]
  }
  ```

GET '/lobbies/:lobby_id/songs' — Returns a list of all songs in lobby with id :lobby_id
  Example: `http POST encoreapp.me/lobbies/298486374/songs` returns
  ```
  [
      {
          "id": 980190964,
          "position": 2,
          "title": "Blank Space",
          "artist": "Taylor Swift",
          "rdio_id": "1234567",
          "vote_count": 3
      }
  ]
  ```

POST '/lobbies/:lobby_id/songs' — Adds a new song to the lobby.
Its postion will be last in the lobby and its vote count will be one (we assume the person adding the song upvotes).
Request must include title, artist, and rdio_id of the song
  Example: `http POST encoreapp.me/lobbies/298486374/songs title=Hello artist=Adele rdio_id=123456` returns
  ```
  {
      "id": 980190964,
      "position": 3,
      "title": "Hello",
      "artist": "Adele",
      "rdio_id": "1234567",
      "vote_count": 1
  }
  ```

DELETE '/lobbies/:lobby_id/songs/:id' — Deletes song with :id from lobby with id :lobby_id
  Example: `http DELETE encoreapp.me/lobbies/298486374/songs/980190964` returns
  HTTP 200 Success

POST '/lobbies/:lobby_id/songs/:id/up' — Upvotes song with id :id
  Example: `http POST encoreapp.me/lobbies/298486374/songs/980190964/up` returns
  HTTP 200 Success

POST '/lobbies/:lobby_id/songs/:id/down' — Downvotes song with id :id
  Example: `http POST encoreapp.me/lobbies/298486374/songs/980190964/down` returns
  HTTP 200 Success
