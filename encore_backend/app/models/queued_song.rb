class QueuedSong < ActiveRecord::Base
  belongs_to :lobby

  validates :position, uniqueness: { scope: :lobby_id }
end
