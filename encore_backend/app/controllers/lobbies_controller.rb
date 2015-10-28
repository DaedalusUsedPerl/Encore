class LobbiesController < ApplicationController
  skip_before_filter :verify_authenticity_token # No way for our AJAX clients to grab this

  # GET /lobby
  #
  # Fetch all lobbies and return them as a JSON object
  # @TODO Use a more sane policy than returning all lobbies
  def index
    render json: Lobby.all
  end

  # POST /lobby
  #
  # Create a new lobby
  # Request must include a name and owner_id corresponding to the user_id of the creator
  # @TODO Authenticate the user creating the lobby
  def create
    owner = User.find params[:owner_id]
    name = params[:name]

    lobby = Lobby.create! owner: owner, name: name
    render json: lobby
  end
end
