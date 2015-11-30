package cs130project.encore;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rdio.android.audioplayer.interfaces.AudioError;
import com.rdio.android.core.RdioApiResponse;
import com.rdio.android.core.RdioService_Api;
import com.rdio.android.sdk.PlayerListener;
import com.rdio.android.sdk.PlayerManager;
import com.rdio.android.sdk.model.Track;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import cz.msebera.android.httpclient.Header;

public class LobbyActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener, PlayerListener {

    private Lobby mLobby;
    private ArrayList<Song> mSongs = new ArrayList<Song>();
    private PlayerManager mPlayerManager;
    private LoadImageViewTask mLoadImageViewTask;

    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mAlbumTextView;
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

        // List
        mRefreshWrapper = (RefreshableListViewWrapper) findViewById(R.id.refresh_wrapper);
        mRefreshWrapper.setOnRefreshListener(this);
        // Adapter
        mListAdapter = new SongAdapter(this, R.layout.song_item, mSongs);
        mListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                // sort list
                mListAdapter.unregisterDataSetObserver(this);
                mListAdapter.sort(new Comparator<Song>() {
                    @Override
                    public int compare(Song lhs, Song rhs) {
                        return rhs.getVoteCount() - lhs.getVoteCount();
                    }
                });
                mListAdapter.registerDataSetObserver(this);
            }
            @Override
            public void onInvalidated() {
            }
        });
        mRefreshWrapper.getListView().setAdapter(mListAdapter);

        // Header
        View headerView = (View)getLayoutInflater().inflate(R.layout.list_header, mRefreshWrapper.getListView(), false);
        mHeaderTextView = (TextView) headerView.findViewById(R.id.text_view);
        mRefreshWrapper.getListView().addHeaderView(headerView, null, false);

        // Player
        CurrentUser.getInstance().getRdio().getPlayerManager().addPlayerListener(this);
        mLoadImageViewTask = new LoadImageViewTask();

        // Track details
        mImageView = (ImageView) findViewById(R.id.image_view);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mArtistTextView = (TextView) findViewById(R.id.artist_text_view);
        mAlbumTextView = (TextView) findViewById(R.id.album_text_view);

        findViewById(R.id.add_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LobbyActivity.this.onSearchRequested();
            }
        });
        findViewById(R.id.play_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LobbyActivity.this.playPause();
            }
        });
        findViewById(R.id.pause_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LobbyActivity.this.playPause();
            }
        });
        findViewById(R.id.next_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LobbyActivity.this.next();
            }
        });

        // Load data
        mRefreshWrapper.setRefreshing(true);
        onRefresh();

        // Auto-refresh
        final Handler autoRefresh = new Handler();
        final int autoRefreshDelay = 5000; // ms
        autoRefresh.postDelayed(new Runnable() {
            public void run() {
                //do something
                LobbyActivity.this.onRefresh();
                autoRefresh.postDelayed(this, autoRefreshDelay);
            }
        }, autoRefreshDelay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if returning from search
        Track track = SearchActivity.getSelectedTrack();
        if (track != null) {
            SearchActivity.clearSelectedTrack();
            new Song(track, mLobby, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == 0) {
                        // success
                        mListAdapter.add((Song) msg.obj);
                    } else {
                        // failure
                        mHeaderTextView.setVisibility(View.VISIBLE);
                        mHeaderTextView.setText("Error adding song: " + msg.obj);
                    }
                    return true;
                }
            });
        }
        // Update
        updateViews();
    }

    private void updateViews() {
        updateTrackDisplay();
        updatePlayerVisibility();
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
                    if (mLobby == null) {
                        mLobby = new Lobby(lobby);
                    } else {
                        mLobby.update(lobby);
                    }
                    // Info
                    setTitle(mLobby.getName());
                    updateViews();
                    // header
                    mHeaderTextView.setText("No more songs");
                    if (mLobby.getQueue().size() == 0) {
                        mHeaderTextView.setVisibility(View.VISIBLE);
                    } else {
                        mHeaderTextView.setVisibility(View.GONE);
                    }
                    // list
                    mListAdapter.clear();
                    mListAdapter.addAll(mLobby.getQueue());
                    // player
                    if (mLobby.getIsHost()) {

                    }
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Player

    private void updatePlayerVisibility() {
        if (mLobby != null && mLobby.getIsHost()) {
            // Host
            if (mLobby.equals(LobbyPlayer.getInstance().getLobby())) {
                // Current lobby
                if (LobbyPlayer.getInstance().isPlaying()) {
                    findViewById(R.id.play_fab).setVisibility(View.INVISIBLE);
                    findViewById(R.id.pause_fab).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.play_fab).setVisibility(View.VISIBLE);
                    findViewById(R.id.pause_fab).setVisibility(View.INVISIBLE);
                }
                findViewById(R.id.next_fab).setVisibility(View.VISIBLE);
            } else {
                // Other lobby
                findViewById(R.id.play_fab).setVisibility(View.VISIBLE);
                findViewById(R.id.pause_fab).setVisibility(View.INVISIBLE);
                findViewById(R.id.next_fab).setVisibility(View.INVISIBLE);
            }
        } else {
            // Not host
            findViewById(R.id.play_fab).setVisibility(View.INVISIBLE);
            findViewById(R.id.pause_fab).setVisibility(View.INVISIBLE);
            findViewById(R.id.next_fab).setVisibility(View.INVISIBLE);
        }
    }

    private void playPause() {
        // Set lobby if needed before playing
        if (mLobby != null && mLobby.equals(LobbyPlayer.getInstance().getLobby())) {
            LobbyPlayer.getInstance().playPause();
        } else {
            LobbyPlayer.getInstance().setLobby(mLobby);
            LobbyPlayer.getInstance().next();
        }
    }

    private void next() {
        LobbyPlayer.getInstance().next();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PlayerListener

    public void onPrepared() {
        updateViews();
    }

    public void onComplete() {
        updateViews();
    }

    public void onError(AudioError error) {
        updateTrackDisplayError(error.getDescription());
    }

    public void onPositionUpdate(int position) {
    }

    public void onPlayStateChanged(PlayerListener.PlayState state) {
        updateViews();
    }

    public void onBufferingStarted() {
    }

    public void onBufferingEnded() {
    }

    public void onSeekStarted() {
    }

    public void onSeekCompleted() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Track

    private void updateTrackDisplay() {
        Track track = LobbyPlayer.getInstance().getCurrentTrack();
        if (mLobby == null) {
            // loading
            updateTrackDisplay(null);
        } else if (track == null || !mLobby.equals(LobbyPlayer.getInstance().getLobby())) {
            Song song = mLobby.getQueue().peek();
            if (song == null) {
                // empty queue
                updateTrackDisplay(null);
            } else {
                // show now playing (non-host)
                song.getTrackAsync(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (msg.what == 0) { // success
                            updateTrackDisplay((Track) msg.obj);
                        } else { // failure
                            updateTrackDisplayError((String) msg.obj);
                        }
                        return true;
                    }
                });
            }
        } else {
            // show now playing (host)
            updateTrackDisplay(track);
        }
    }

    private void updateTrackDisplay(Track track) {
        if (track != null) {
            mTitleTextView.setText(track.name);
            mArtistTextView.setText(track.artistName);
            mAlbumTextView.setText(track.albumName);
            mLoadImageViewTask.cancel(true);
            mLoadImageViewTask = new LoadImageViewTask();
            mLoadImageViewTask.execute(mImageView, track);
        } else {
            mTitleTextView.setText(null);
            mArtistTextView.setText(null);
            mAlbumTextView.setText(null);
            clearImageView();
        }
    }

    private void updateTrackDisplayError(String description) {
        mTitleTextView.setText("Error:");
        mArtistTextView.setText(description);
        mAlbumTextView.setText(null);
        clearImageView();
    }

    private void clearImageView() {
        mLoadImageViewTask.cancel(true);
        mImageView.setImageDrawable(null);
    }

    private class LoadImageViewTask extends AsyncTask<Object, Void, Void> {
        ImageView mImageView;
        Drawable mDrawable;

        protected Void doInBackground(Object... params) {
            mImageView = (ImageView) params[0];
            Track track = (Track) params[1];

            try {
                URL url = new URL(track.icon200px);
                mDrawable = Drawable.createFromStream(url.openStream(), "src");
            } catch (MalformedURLException e) {
                mDrawable = null;
            } catch (IOException e) {
                mDrawable = null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            mImageView.setImageDrawable(mDrawable);
        }
    }
}
