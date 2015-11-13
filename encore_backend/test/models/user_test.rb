require 'test_helper'

class UserTest < ActiveSupport::TestCase
  test "has many lobbies" do
    user = User.create! name: "This Guy"
    lobby1 = Lobby.create! owner: user, name: "My Lobby"
    lobby2 = Lobby.create! owner: user, name: "My 2nd Lobby"
    user.reload

    assert_equal [lobby1, lobby2], user.lobbies
  end

  test "find_or_create find" do
    user = User.create!(unique_id: "applesandbananas")
    assert_equal user, User.find_or_create("applesandbananas")
  end

  test "find_or_create create" do
    user = nil
    assert_difference "User.count" do
      user = User.find_or_create("applesandbananas")
    end
    assert "applesandbananas", user.unique_id
  end
end
