package cs130project.encore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rdio.android.sdk.model.Track;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LobbyActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private Lobby mLobby;
    private ArrayList<Song> mSongs = new ArrayList<Song>();

    private TextView mHeaderTextView;
    private RefreshableListViewWrapper mRefreshWrapper;
    private SongAdapter mListAdapter;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Loading...");

        findViewById(R.id.add_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LobbyActivity.this.onSearchRequested();
            }
        });
        findViewById(R.id.play_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentUser.getInstance().setHostingLobby(mLobby);
            }
        });

        mRefreshWrapper = (RefreshableListViewWrapper) findViewById(R.id.refresh_wrapper);
        mRefreshWrapper.setOnRefreshListener(this);
        mRefreshWrapper.getListView().setOnItemClickListener(this);

        View headerView = (View)getLayoutInflater().inflate(R.layout.list_header, mRefreshWrapper.getListView(), false);
        mHeaderTextView = (TextView) headerView.findViewById(R.id.text_view);
        mRefreshWrapper.getListView().addHeaderView(headerView, null, false);

        mListAdapter = new SongAdapter(this, R.layout.song_item, mSongs);
        mRefreshWrapper.getListView().setAdapter(mListAdapter);

        // Load data
        mRefreshWrapper.setRefreshing(true);
        onRefresh();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // List

    @Override
    public void onRefresh() {
        String id = getIntent().getStringExtra("lobbyId");
        if (id != null) {
            Api.get("lobbies/" + id, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject lobby) {
                    mLobby = new Lobby(lobby);
                    setTitle(mLobby.getName());
                    // header
                    mHeaderTextView.setText("No songs");
                    if (mLobby.getQueue().size() == 0) {
                        mHeaderTextView.setVisibility(View.VISIBLE);
                    } else {
                        mHeaderTextView.setVisibility(View.GONE);
                    }
                    // list
                    mListAdapter.clear();
                    mListAdapter.addAll(mLobby.getQueue());
                    // refresh
                    mRefreshWrapper.setRefreshing(false);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    mLobby = null;
                    mHeaderTextView.setVisibility(View.VISIBLE);
                    mHeaderTextView.setText("Error: " + responseString);
                    mRefreshWrapper.setRefreshing(false);
                }
            });
        } else {
            mLobby = null;
            mHeaderTextView.setVisibility(View.VISIBLE);
            mHeaderTextView.setText("Error: no lobby id");
            mRefreshWrapper.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int cellPosition, long id) {
        final int position = cellPosition - 1;
        final Song song = mSongs.get(position);
        // TODO: anything on item click?
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Add songs

    @Override
    protected void onResume() {
        super.onResume();
        Track track = SearchActivity.getSelectedTrack();
        SearchActivity.clearSelectedTrack();
        if (track != null) {
            addSong(new Song(track));
        }
    }

    public void addSong(final Song song) {
        RequestParams params = new RequestParams();
        params.put("title", song.getTitle());
        params.put("artist", song.getArtist());
        params.put("rdio_id", song.getRdioId());
        // TODO: not working (422)
        Api.post("lobbies/" + mLobby.getId() + "/songs", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mListAdapter.add(song);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mHeaderTextView.setVisibility(View.VISIBLE);
                mHeaderTextView.setText("Error adding song: " + responseString);
            }
        });
    }
}
