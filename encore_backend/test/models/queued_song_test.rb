require 'test_helper'

class QueuedSongTest < ActiveSupport::TestCase
  test "belongs to lobby" do
    lobby = Lobby.create! owner: User.first, name: "I like turtles"
    qs = QueuedSong.create! lobby: lobby, position: 1, title: "Hello", vote_count: 9000
    qs.reload
    assert_equal lobby, qs.lobby
  end

  test "only one song per position in each lobby" do
    lobby = Lobby.create! owner: User.first, name: "I like turtles"
    qs1 = QueuedSong.create! lobby: lobby, position: 1, title: "Hello", vote_count: 9000
    qs2 = QueuedSong.new lobby: lobby, position: 1, title: "Good Bye", vote_count: 9000
    assert !qs2.valid?
  end

  test "serializable_hash" do
    lobby = Lobby.create! owner: User.first, name: "I like turtles"
    qs = QueuedSong.create! lobby: lobby, position: 5, title: "Hello",
                            artist: "Adele", vote_count: 9000, rdio_id: "1234567"
    json = qs.as_json
    assert json["id"]
    assert json["title"]
    assert json["artist"]
    assert json["position"]
    assert json["lobby_id"]
    assert json["vote_count"]
    assert json["rdio_id"]
    assert_nil json["created_at"]
  end
end
