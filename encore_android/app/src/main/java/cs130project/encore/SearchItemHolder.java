package cs130project.encore;

import android.view.View;
import android.widget.TextView;

public class SearchItemHolder {

    private View mView;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mAlbumTextView;

    public SearchItemHolder(View view) {
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

    public TextView getAlbumTextView () {
        if (mAlbumTextView == null) {
            mAlbumTextView = (TextView) mView.findViewById(R.id.album_text_view);
        }
        return mAlbumTextView;
    }

}