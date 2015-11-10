require 'test_helper'

class SongsControllerTest < ActionController::TestCase
  def setup
    @lobby = Lobby.create! name: "Hello"
    QueuedSong.create! song: "Heartbeat", lobby_id: @lobby.id, position: 1, vote_count: 0
    QueuedSong.create! song: "You See Me", lobby_id: @lobby.id, position: 2, vote_count: 5
    @lobby.reload
  end

  test "index" do
    get :index, lobby_id: @lobby.id
    assert_response :success

    assert_equal @lobby.queued_songs[0].song, JSON.parse(response.body)[0]["song"]
    assert_equal @lobby.queued_songs[1].song, JSON.parse(response.body)[1]["song"]
  end

  test "create" do
    assert_difference "@lobby.queued_songs.count" do
      post :create, lobby_id: @lobby.id, title: "Hello"
    end
    assert_response :success
    json = JSON.parse response.body
    assert_equal 3, json["position"]
    assert_equal 1, json["vote_count"]
    assert_equal "Hello", json["song"]
  end

  test "index without invalid lobby_id" do
    get :index, lobby_id: 9999
    assert_response :not_found
    assert_equal "", response.body
  end

  test "upvote" do
    song = @lobby.queued_songs[0]
    assert_difference "song.reload.vote_count", 1 do
      post :upvote, lobby_id: @lobby.id, id: song.id
    end
    assert_response :success
  end

  test "downvote" do
    song = @lobby.queued_songs[0]
    assert_difference "song.reload.vote_count", -1 do
      post :downvote, lobby_id: @lobby.id, id: song.id
    end
    assert_response :success
  end
end
