package cs130project.encore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.rdio.android.sdk.PlayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

public class Lobby {
    private String mId;
    private String mName;
    private Queue<Song> mQueue = new LinkedList<Song>();
    private boolean mIsHost;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Object

    Lobby() {
        // TODO: save isHost after new lobby creation flow
        mIsHost = true;
    }

    Lobby(JSONObject json) {
        update(json);
    }

    public void update(JSONObject json) {
        try {
            mId = json.getString("id");
            mName = json.getString("name");
            if (json.has("queued_songs")) {
                mQueue.clear();
                JSONArray songs = json.getJSONArray("queued_songs");
                for (int i = 0; i < songs.length(); i++) {
                    Song song = new Song(songs.getJSONObject(i));
                    Song playingSong = LobbyPlayer.getInstance().getCurrentSong();
                    if (playingSong == null || !song.equals(playingSong)) {
                        mQueue.add(song);
                    } else {
                        Log.i("", "");
                    }
                }
            }
            mIsHost = true; // TODO: getSharedPreferences().getBoolean(getIsHostKey(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Lobby && getId().equals(((Lobby)o).getId()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Accessors

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Queue<Song> getQueue() {
        return mQueue;
    }

    public boolean getIsHost() {
        return mIsHost;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Saved

    private SharedPreferences getSharedPreferences() {
        return App.getContext().getSharedPreferences("lobby", Context.MODE_PRIVATE);
    }

    private String getIsHostKey() {
        return mId;
    }

    private void setIsHost(boolean isHost) {
        mIsHost = isHost;
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(getIsHostKey(), mIsHost);
        editor.commit();
    }

}
