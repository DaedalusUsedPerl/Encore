package cs130project.encore;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.rdio.android.sdk.model.Track;

public class SearchAdapter extends ArrayAdapter<Track> {

    private int mResourceId;
    private LayoutInflater inflater;
    private Context context;

    public SearchAdapter (Context ctx, int resourceId, List objects) {
        super(ctx, resourceId, objects);
        mResourceId = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        Track track = (Track) getItem(position);

        // Get or create cached trackView
        SearchItemHolder trackView;
        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(mResourceId, null);
            trackView = new SearchItemHolder(convertView);
            convertView.setTag(trackView);
        }
        else {
            trackView = (SearchItemHolder) convertView.getTag();
        }

        // Fill data
        trackView.getTitleTextView().setText(track.name);
        trackView.getArtistTextView().setText(track.artistName);
        trackView.getAlbumTextView().setText(track.albumName);

        return convertView;
    }
}