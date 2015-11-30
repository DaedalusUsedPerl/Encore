package cs130project.encore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Allow the user to find or create a loby
 */
public class FindOrCreateLobby extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    static final int NEW_LOBBY_REQUEST_CODE = 1;

    private ArrayList<Lobby> mLobbies = new ArrayList<Lobby>();

    private TextView mHeaderTextView;
    private RefreshableListViewWrapper mRefreshWrapper;
    private LobbyAdapter mListAdapter;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findorcreatelobby);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Available Lobbies");

        findViewById(R.id.now_playing_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLobbyActivity(LobbyPlayer.getInstance().getLobby());
            }
        });

        mRefreshWrapper = (RefreshableListViewWrapper) findViewById(R.id.refresh_wrapper);
        mRefreshWrapper.setOnRefreshListener(this);
        mRefreshWrapper.getListView().setOnItemClickListener(this);

        View headerView = (View)getLayoutInflater().inflate(R.layout.list_header, mRefreshWrapper.getListView(), false);
        mHeaderTextView = (TextView) headerView.findViewById(R.id.text_view);
        mHeaderTextView.setText("Loading...");
        mHeaderTextView.setTextColor(getResources().getColor(R.color.backgroundLight));
        mRefreshWrapper.getListView().addHeaderView(headerView, null, false);

        mListAdapter = new LobbyAdapter(this, R.layout.lobby_item, mLobbies);
        mRefreshWrapper.getListView().setAdapter(mListAdapter);

        // Load data
        mRefreshWrapper.setRefreshing(true);
        onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.now_playing_button).setVisibility(
                LobbyPlayer.getInstance().getLobby() == null ? View.INVISIBLE : View.VISIBLE
        );
        onRefresh();
    }

    private void startLobbyActivity(Lobby lobby) {
        startLobbyActivity(lobby.getId());
    }

    private void startLobbyActivity(String lobbyId) {
        Intent intent = new Intent(FindOrCreateLobby.this, LobbyActivity.class);
        intent.putExtra("lobbyId", lobbyId);
        startActivity(intent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // List

    @Override
    public void onRefresh() {
        Api.get("lobbies", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray lobbies) {
                // header
                mHeaderTextView.setText("No events");
                if (lobbies.length() == 0) {
                    mHeaderTextView.setVisibility(View.VISIBLE);
                } else {
                    mHeaderTextView.setVisibility(View.GONE);
                }
                // list
                mListAdapter.clear();
                for (int i = 0; i < lobbies.length(); i++) {
                    try {
                        mListAdapter.add(new Lobby(lobbies.getJSONObject(i)));
                    } catch (JSONException e) {
                    }
                }
                // refresh
                mRefreshWrapper.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mHeaderTextView.setVisibility(View.VISIBLE);
                mHeaderTextView.setText("Error: " + responseString);
                mRefreshWrapper.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int cellPosition, long id) {
        final int position = cellPosition - 1;
        final Lobby lobby = mLobbies.get(position);
        startLobbyActivity(lobby);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // New Lobby

    public void enterLobbyMaker(View view) {
        Intent intent = new Intent(this, LobbySettings.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == NEW_LOBBY_REQUEST_CODE) {
            startLobbyActivity(data.getStringExtra("lobbyId"));
        }
    }
}
