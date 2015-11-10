class SongsController < ApplicationController
  before_action :load_lobby

  def index
    render json: @lobby.queued_songs.to_json(only: [:id, :song, :position, :vote_count])
  end

  def create
    title = params[:title]
    position = @lobby.queued_songs.maximum(:position) + 1
    song = QueuedSong.create song: title, position: position, vote_count: 1
    puts song.inspect
    @lobby.queued_songs.push song
    render json: song.to_json(only: [:id, :song, :position, :vote_count])
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
