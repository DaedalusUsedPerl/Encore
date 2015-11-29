package cs130project.encore;

import android.content.Context;
import android.content.SharedPreferences;

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

    Lobby() {
        // TODO: save isHost after new lobby creation flow
        mIsHost = true;
    }

    Lobby(JSONObject json) {
        try {
            mId = json.getString("id");
            mName = json.getString("name");
            if (json.has("queued_songs")) {
                JSONArray songs = json.getJSONArray("queued_songs");
                for (int i = 0; i < songs.length(); i++) {
                    JSONObject song = songs.getJSONObject(i);
                    mQueue.add(new Song(song));
                }
            }
            mIsHost = getSharedPreferences().getBoolean(getIsHostKey(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
