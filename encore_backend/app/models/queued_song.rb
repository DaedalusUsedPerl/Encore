class QueuedSong < ActiveRecord::Base
  belongs_to :lobby

  validates :position, uniqueness: { scope: :lobby_id }

  def serializable_hash(options = nil)
    super({only: [:id, :title, :artist, :position, :vote_count, :lobby_id, :rdio_id]}.merge(options || {}))
  end
end
