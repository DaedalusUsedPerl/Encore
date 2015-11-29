package cs130project.encore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.rdio.android.sdk.model.Track;

import java.util.List;

public class LobbyAdapter extends ArrayAdapter<Lobby> {

    private int mResourceId;
    private LayoutInflater inflater;
    private Context context;

    public LobbyAdapter(Context ctx, int resourceId, List objects) {
        super(ctx, resourceId, objects);
        mResourceId = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        Lobby lobby = (Lobby) getItem(position);

        // Get or create cached trackView
        LobbyItemHolder lobbyView;
        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(mResourceId, null);
            lobbyView = new LobbyItemHolder(convertView);
            convertView.setTag(lobbyView);
        }
        else {
            lobbyView = (LobbyItemHolder) convertView.getTag();
        }

        // Fill data
        lobbyView.getTitleTextView().setText(lobby.getName());

        return convertView;
    }
}