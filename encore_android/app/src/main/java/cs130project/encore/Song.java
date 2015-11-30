package cs130project.encore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rdio.android.core.RdioApiResponse;
import com.rdio.android.core.RdioService_Api;
import com.rdio.android.sdk.PlayRequest;
import com.rdio.android.sdk.model.Track;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Song {
    private String mId;
    private String mTitle;
    private String mArtist;
    private int mVoteCount;
    private String mLobbyId;
    private String mRdioId;
    private int mCurrentUserVoteCount;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Object

    /**
     * Create new Song from api response
     */
    Song(JSONObject json) {
        try {
            mId = json.getString("id");
            mTitle = json.getString("title");
            mArtist = json.getString("artist");
            mVoteCount = json.getInt("vote_count");
            mLobbyId = json.getString("lobby_id");
            mRdioId = json.getString("rdio_id");
            mCurrentUserVoteCount = getSharedPreferences().getInt(getCurrentUserVoteCountKey(), 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create new Song with the given track to add to the given lobby
     */
    Song(Track track, Lobby lobby, final Handler.Callback callback) {
        mId = null;
        mTitle = track.name;
        mArtist = track.artistName;
        mVoteCount = 1;
        mLobbyId = lobby.getId();
        mRdioId = track.key;
        mCurrentUserVoteCount = 1;

        RequestParams params = new RequestParams();
        params.put("title", mTitle);
        params.put("artist", mArtist);
        params.put("rdio_id", mRdioId);
        Api.post("lobbies/" + lobby.getId() + "/songs", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    mId = response.getString("id");
                    saveCurrentUserVoteCount();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.handleMessage(Message.obtain(null, 0, Song.this));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.handleMessage(Message.obtain(null, -1, responseString));
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Song && getId().equals(((Song)o).getId()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Play

    public void markPlayed() {
        Api.delete("lobbies/" + mLobbyId + "/songs/" + mId, new JsonHttpResponseHandler());
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(getCurrentUserVoteCountKey());
        editor.commit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Track

    public void getTrackAsync(final Handler.Callback callback) {
        ArrayList<String> keys = new ArrayList<String>();
        keys.add(mRdioId);
        CurrentUser.getInstance().getRdioService().get(keys, null, null, false, null, new RdioService_Api.ResponseListener() {
            @Override
            public void onResponse(RdioApiResponse rdioApiResponse) {
                if (rdioApiResponse.isSuccess()) {
                    JSONObject json = rdioApiResponse.getResult();
                    try {
                        Track track = Track.extractTrack(json.getJSONObject(mRdioId));
                        callback.handleMessage(Message.obtain(null, 0, track));
                    } catch (JSONException e) {
                        callback.handleMessage(Message.obtain(null, -1, "No track found"));
                    }
                } else {
                    callback.handleMessage(Message.obtain(null, -1, rdioApiResponse.getErrorMessage()));
                }
            }
        });
    }

    public PlayRequest getPlayRequest() {
        return new PlayRequest(mRdioId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Accessors


    public String getId() {
        return mId;
    }

    public String getRdioId() {
        return mRdioId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist != null ? mArtist : "Unknown";
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public int getCurrentUserVoteCount() {
        return mCurrentUserVoteCount;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Votes

    private SharedPreferences getSharedPreferences() {
        return App.getContext().getSharedPreferences("song", Context.MODE_PRIVATE);
    }

    private String getCurrentUserVoteCountKey() {
        return mId + "|" + mLobbyId;
    }

    private void saveCurrentUserVoteCount() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(getCurrentUserVoteCountKey(), mCurrentUserVoteCount);
        editor.commit();
    }

    private void vote(final int value, final Handler.Callback updated) {
        String direction = null;
        if (value == mCurrentUserVoteCount) {
            return;
        } else if (value == -1) {
            direction = "down";
        } else if (value == 1) {
            direction = "up";
        } else {
            return;
        }

        // optimisitic
        mCurrentUserVoteCount += value;
        mVoteCount += value;
        updated.handleMessage(null);
        // api
        Api.post("lobbies/"+mLobbyId+"/songs/"+mId+"/"+direction, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                saveCurrentUserVoteCount();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 200) { // not a failure
                    saveCurrentUserVoteCount();
                } else {
                    updated.handleMessage(null);
                    mCurrentUserVoteCount -= value;
                    mVoteCount -= value;
                }
            }
        });
    }

    public void voteUp(Handler.Callback updated) {
        vote(1, updated);
    }

    public void voteDown(Handler.Callback updated) {
        vote(-1, updated);
    }

    public void clearVote(Handler.Callback updated) {
        vote(-mCurrentUserVoteCount, updated);
    }
}
