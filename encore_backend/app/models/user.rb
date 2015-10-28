class User < ActiveRecord::Base
  has_many :lobbies, foreign_key: "owner_id"
end
