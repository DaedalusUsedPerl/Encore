package cs130project.encore;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SongItemHolder {

    private View mView;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mVoteCountTextView;
    private Button mUpvoteButton;
    private Button mDownvoteButton;

    public SongItemHolder(View view) {
        mView = view;
    }

    public View getView() {
        return mView;
    }

    public TextView getTitleTextView () {
        if (mTitleTextView == null) {
            mTitleTextView = (TextView) mView.findViewById(R.id.title_text_view);
        }
        return mTitleTextView;
    }

    public TextView getArtistTextView () {
        if (mArtistTextView == null) {
            mArtistTextView = (TextView) mView.findViewById(R.id.artist_text_view);
        }
        return mArtistTextView;
    }

    public TextView getVoteCountTextView () {
        if (mVoteCountTextView == null) {
            mVoteCountTextView = (TextView) mView.findViewById(R.id.vote_count_text_view);
        }
        return mVoteCountTextView;
    }

    public Button getUpvoteButton () {
        if (mUpvoteButton == null) {
            mUpvoteButton = (Button) mView.findViewById(R.id.upvote_button);
        }
        return mUpvoteButton;
    }

    public Button getDownvoteButton () {
        if (mDownvoteButton == null) {
            mDownvoteButton = (Button) mView.findViewById(R.id.downvote_button);
        }
        return mDownvoteButton;
    }

}