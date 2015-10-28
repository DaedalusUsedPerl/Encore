Rails.application.routes.draw do
  get 'lobbies' => 'lobbies#index'
  post 'lobbies' => 'lobbies#create'
end
