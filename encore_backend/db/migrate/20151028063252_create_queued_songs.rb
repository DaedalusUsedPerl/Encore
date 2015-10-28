class CreateQueuedSongs < ActiveRecord::Migration
  def change
    create_table :queued_songs do |t|
      t.references :lobby, foreign_key: true
      t.integer :position
      t.integer :vote_count
      t.string :song

      t.timestamps null: false
    end

    add_index :queued_songs, [:lobby_id, :position], :unique => true
  end
end
