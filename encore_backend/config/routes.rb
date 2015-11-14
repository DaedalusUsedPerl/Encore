Rails.application.routes.draw do
  resources :lobbies, only: [:index, :create, :show] do
    resources :songs, only: [:index, :create, :destroy] do
      member do
        post 'up' => 'songs#upvote'
        post 'down' => 'songs#downvote'
      end
    end
  end
end
