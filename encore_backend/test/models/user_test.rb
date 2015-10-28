require 'test_helper'

class UserTest < ActiveSupport::TestCase
  test "has many lobbies" do
    user = User.create! name: "This Guy"
    lobby1 = Lobby.create! owner: user, name: "My Lobby"
    lobby2 = Lobby.create! owner: user, name: "My 2nd Lobby"
    user.reload

    assert_equal [lobby1, lobby2], user.lobbies
  end
end
