Rails.application.routes.draw do
  resources :lobbies, only: [:index, :create, :show]
end
