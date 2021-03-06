class LobbiesController < ApplicationController
  skip_before_filter :verify_authenticity_token # No way for our AJAX clients to grab this

  # GET /lobbies
  #
  # Fetch all lobbies and return them as a JSON object
  # @TODO Use a more sane policy than returning all lobbies
  def index
    render json: Lobby.all.as_json( only: [:id, :name] )
  end

  # POST /lobbies
  #
  # Create a new lobby
  # Request must include a name and owner_id corresponding to the unique_id of the creator
  # @TODO Authenticate the user creating the lobby
  def create
    owner = User.find_or_create params[:owner_id]
    name = params[:name]

    lobby = Lobby.create! owner: owner, name: name
    render json: lobby.as_json( only: [:id, :name] )
  end

  def show
    lobby = Lobby.find params[:id]
    render json: lobby.as_json(
               only: [:id, :name],
               include: :queued_songs
           )
  rescue ActiveRecord::RecordNotFound => e
    head :not_found
  end
end
