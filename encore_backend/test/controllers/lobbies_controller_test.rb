require 'test_helper'

class LobbiesControllerTest < ActionController::TestCase
  test "index" do
    get :index
    assert_response :success

    lobbies = JSON.parse response.body
    assert_equal "Lobby2", lobbies[0]["name"]

    # @TODO make sure everything else is correct
  end

  test "create" do
    owner = User.first

    assert_difference 'Lobby.count', 1 do
      post :create, owner_id: owner.id, name: "My Little Lobby"
    end
    assert_response :success

    json = JSON.parse response.body
    assert_equal owner.id, json["owner_id"]
    assert_equal "My Little Lobby", json["name"]
  end

  test "show" do
    lobby = Lobby.first
    lobby.queued_songs.push(QueuedSong.create! song: "Youre Beautiful", position: 1, lobby: lobby)
    get :show, id: lobby.id
    assert_response :success

    json = JSON.parse response.body
    assert_equal "Youre Beautiful", json["queued_songs"][0]["song"]
  end
end
