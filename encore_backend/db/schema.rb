# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20151028063252) do

  create_table "lobbies", force: :cascade do |t|
    t.string   "name"
    t.integer  "owner_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "lobbies", ["owner_id"], name: "index_lobbies_on_owner_id"

  create_table "queued_songs", force: :cascade do |t|
    t.integer  "lobby_id"
    t.integer  "position"
    t.integer  "vote_count"
    t.string   "song"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "queued_songs", ["lobby_id", "position"], name: "index_queued_songs_on_lobby_id_and_position", unique: true

  create_table "users", force: :cascade do |t|
    t.string   "name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

end
