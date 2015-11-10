require 'test_helper'

class LobbyTest < ActiveSupport::TestCase
  test "lobby has owner" do
    user = User.create! name: "Bobby Tables"
    lobby = Lobby.create! owner: user, name: "Sick Beats by Tables(tm)(r)"
    lobby.reload
    assert_equal user, lobby.owner
  end

  test "lobby has queued songs" do
    lobby = Lobby.create! owner: User.first, name: "I like turtles"
    qs1 = QueuedSong.create! lobby: lobby, position: 1, title: "Hello", vote_count: 9000
    qs2 = QueuedSong.create! lobby: lobby, position: 2, title: "Good Bye", vote_count: 9000
    lobby.reload
    assert_equal [qs1, qs2], lobby.queued_songs
  end
end
