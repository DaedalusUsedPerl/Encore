package cs130project.encore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.rdio.android.sdk.model.Track;

import java.util.List;

/**
 * Adapter to mimic an array of lobbies
 */
public class LobbyAdapter extends ArrayAdapter<Lobby> {

    private int mResourceId;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Instantiate an adapter to hold all lobbies.
     * @param ctx The context in which this adapter is used
     * @param resourceId The resource ID for the view to display the lobbies
     * @param objects A list containing the lobbies that backs the adapter.
     */
    public LobbyAdapter(Context ctx, int resourceId, List objects) {
        super(ctx, resourceId, objects);
        mResourceId = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    /**
     * Generate a custom view to display a lobby
     * @param position The position of the lobby in the backing list to display
     * @param convertView The old view to reuse
     * @param parent The parent this view will be attached to
     * @return A View corresponding to the lobby at index position
     */
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