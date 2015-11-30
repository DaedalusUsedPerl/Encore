package cs130project.encore;

import android.view.View;
import android.widget.TextView;

/**
 * Hold a view representing a lobby
 */
public class LobbyItemHolder {

    private View mView;
    private TextView mTitleTextView;

    /**
     * Instantiate from a view
     * @param view A view representing the lobby
     */
    public LobbyItemHolder(View view) {
        mView = view;
    }

    /**
     * Get the view instantiated with
     * @return The view for the lobby
     */
    public View getView() {
        return mView;
    }

    /**
     * Get a view representing the title text, instantiated lazily.
     * @return The TitleTextView for this lobby.
     */
    public TextView getTitleTextView () {
        if (mTitleTextView == null) {
            mTitleTextView = (TextView) mView.findViewById(R.id.title_text_view);
        }
        return mTitleTextView;
    }

}