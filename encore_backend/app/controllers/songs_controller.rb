class SongsController < ApplicationController
  skip_before_filter :verify_authenticity_token
  before_action :load_lobby

  def index
    render json: @lobby.queued_songs.as_json
  end

  def create
    title = params[:title]
    artist = params[:artist]
    rdio_id = params[:rdio_id]
    position = @lobby.queued_songs.maximum(:position) + 1
    song = QueuedSong.create title: title, artist: artist, position: position,
                             vote_count: 1, rdio_id: rdio_id
    @lobby.queued_songs.push song
    render json: song.as_json
  end

  def destroy
    @lobby.queued_songs.find(params[:id]).destroy
    head :ok
  end

  def upvote
    @lobby.queued_songs.find(params[:id]).increment!(:vote_count, 1)
    head :ok
  end

  def downvote
    @lobby.queued_songs.find(params[:id]).increment!(:vote_count, -1)
    head :ok
  end

private
  def load_lobby
    @lobby = Lobby.find params[:lobby_id]
  rescue ActiveRecord::RecordNotFound => e
    head :not_found
  end
end
