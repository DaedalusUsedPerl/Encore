class ChangeQueuedSongsSchema < ActiveRecord::Migration
  def change
    rename_column :queued_songs, :song, :title
    add_column :queued_songs, :artist, :string
    add_column :queued_songs, :rdio_id, :string
  end
end
