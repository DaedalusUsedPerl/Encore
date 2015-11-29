package cs130project.encore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rdio.android.core.RdioApiResponse;
import com.rdio.android.core.RdioService_Api;
import com.rdio.android.sdk.OAuth2Credential;
import com.rdio.android.sdk.PlayRequest;
import com.rdio.android.sdk.model.Track;

import org.json.JSONArray;
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

    Song(Track track, Lobby lobby) {
        mId = null;
        mTitle = track.name;
        mArtist = track.artistName;
        mVoteCount = 0;
        mLobbyId = lobby.getId();
        mRdioId = track.key;
        mCurrentUserVoteCount = getSharedPreferences().getInt(getCurrentUserVoteCountKey(), 0);
    }

    public void markPlayed() {
        Api.delete("lobbies/" + mLobbyId + "/songs/" + mId, new JsonHttpResponseHandler());
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(getCurrentUserVoteCountKey());
        editor.commit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Track

    public void getTrackAsync(RdioService_Api.ResponseListener listener) {
        ArrayList<String> keys = new ArrayList<String>();
        keys.add("t" + mId);
        CurrentUser.getInstance().getRdioService().get(keys, null, null, false, null, listener);
    }

    public PlayRequest getPlayRequest() {
        return new PlayRequest(mRdioId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Accessors

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
