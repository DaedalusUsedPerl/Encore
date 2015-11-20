package cs130project.encore;

import com.rdio.android.sdk.PlayRequest;
import com.rdio.android.sdk.model.Track;

import org.json.JSONException;
import org.json.JSONObject;

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
            mCurrentUserVoteCount = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Song(Track track) {
        mId = null;
        mTitle = track.name;
        mArtist = track.artistName;
        mVoteCount = 0;
        mLobbyId = null;
        mRdioId = track.key;
        mCurrentUserVoteCount = 0;
    }

    public PlayRequest getPlayRequest() {
        return new PlayRequest(mRdioId);
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
        return mVoteCount + mCurrentUserVoteCount;
    }

    public int getCurrentUserVoteCount() {
        return mCurrentUserVoteCount;
    }

    public void voteUp() {
        mCurrentUserVoteCount = 1;
    }

    public void voteDown() {
        mCurrentUserVoteCount = -1;
    }

    public void clearVote() {
        mCurrentUserVoteCount = 0;
    }
}
