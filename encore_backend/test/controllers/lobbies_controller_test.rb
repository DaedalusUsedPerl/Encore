require 'test_helper'

class LobbiesControllerTest < ActionController::TestCase
  test "index" do
    get :index
    assert_response :success

    lobbies = JSON.parse response.body
    assert_equal "Lobby2", lobbies[0]["name"]
  end

  test "create" do
    owner = User.first

    assert_difference 'Lobby.count', 1 do
      post :create, owner_id: owner.unique_id, name: "My Little Lobby"
    end
    assert_response :success

    json = JSON.parse response.body
    assert_equal "My Little Lobby", json["name"]
  end

  test "show" do
    lobby = Lobby.first
    lobby.queued_songs.push(QueuedSong.create! title: "Youre Beautiful", position: 3, lobby: lobby)
    get :show, id: lobby.id
    assert_response :success

    json = JSON.parse response.body
    assert_equal "Youre Beautiful", json["queued_songs"][1]["title"]
    assert_nil json["queued_songs"][0]["created_at"]
  end
end
