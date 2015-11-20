package cs130project.encore;

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

    Lobby() {}

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
}
