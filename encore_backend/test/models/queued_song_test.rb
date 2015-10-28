require 'test_helper'

class QueuedSongTest < ActiveSupport::TestCase
  test "belongs to lobby" do
    lobby = Lobby.create! owner: User.first, name: "I like turtles"
    qs = QueuedSong.create! lobby: lobby, position: 1, song: "Hello", vote_count: 9000
    qs.reload
    assert_equal lobby, qs.lobby
  end

  test "only one song per position in each lobby" do
    lobby = Lobby.create! owner: User.first, name: "I like turtles"
    qs1 = QueuedSong.create! lobby: lobby, position: 1, song: "Hello", vote_count: 9000
    qs2 = QueuedSong.new lobby: lobby, position: 1, song: "Good Bye", vote_count: 9000
    assert !qs2.valid?
  end
end
