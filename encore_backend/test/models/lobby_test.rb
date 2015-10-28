require 'test_helper'

class LobbyTest < ActiveSupport::TestCase
  test "lobby has owner" do
    user = User.create! name: "Bobby Tables"
    lobby = Lobby.create! owner: user, name: "Sick Beats by Tables(tm)(r)"
    lobby.reload
    assert_equal user, lobby.owner
  end
end
