package cs130project.encore;

import android.view.View;
import android.widget.TextView;

public class LobbyItemHolder {

    private View mView;
    private TextView mTitleTextView;

    public LobbyItemHolder(View view) {
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

}