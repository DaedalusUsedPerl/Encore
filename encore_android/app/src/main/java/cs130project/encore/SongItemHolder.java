package cs130project.encore;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SongItemHolder {

    private View mView;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mVoteCountTextView;
    private ImageButton mUpvoteButton;
    private ImageButton mDownvoteButton;

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

    public ImageButton getUpvoteButton () {
        if (mUpvoteButton == null) {
            mUpvoteButton = (ImageButton) mView.findViewById(R.id.upvote_button);
        }
        return mUpvoteButton;
    }

    public ImageButton getDownvoteButton () {
        if (mDownvoteButton == null) {
            mDownvoteButton = (ImageButton) mView.findViewById(R.id.downvote_button);
        }
        return mDownvoteButton;
    }

}