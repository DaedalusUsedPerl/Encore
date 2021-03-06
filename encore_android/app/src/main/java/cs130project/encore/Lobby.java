package cs130project.encore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rdio.android.sdk.PlayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

import cz.msebera.android.httpclient.Header;

/**
 * Struct class to hold information on a lobby.
 */
public class Lobby {
    private String mId;
    private String mName;
    private Queue<Song> mQueue = new LinkedList<Song>();
    private boolean mIsHost;

    /**
     * Instantiate a lobby with a given name
     * @param name The name of this lobby.
     * @param callback A callback for notifying lobby instantiation or failure
     */
    Lobby(String name, final Handler.Callback callback) {
        mName = name;
        mIsHost = true;

        RequestParams params = new RequestParams();
        params.put("name", mName);
        Api.post("lobbies", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    mId = response.getString("id");
                    saveIsHost();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.handleMessage(Message.obtain(null, 0, Lobby.this));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.handleMessage(Message.obtain(null, -1, responseString));
            }
        });
    }

    /**
     * Instantiate a lobby from a JSON response from the backend
     * @param json The JSON object holding this lobby's data
     */
    Lobby(JSONObject json) {
        update(json);
    }

    /**
     * Update this lobby with information from the server
     * @param json New information from the server to update this lobby with.
     */
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
                    }
                }
            }
            mIsHost = getSharedPreferences().getBoolean(getIsHostKey(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o != null && o instanceof Lobby && getId().equals(((Lobby)o).getId()));
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

    private void saveIsHost() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(getIsHostKey(), mIsHost);
        editor.commit();
    }

}
