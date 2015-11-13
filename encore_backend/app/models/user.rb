class User < ActiveRecord::Base
  has_many :lobbies, foreign_key: "owner_id"

  def self.find_or_create(unique_id)
    user = User.find_by_unique_id unique_id
    user = User.create unique_id: unique_id unless user
    user
  end
end
