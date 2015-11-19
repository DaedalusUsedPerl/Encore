package cs130project.encore;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdio.android.audioplayer.interfaces.AudioError;
import com.rdio.android.sdk.PlayRequest;
import com.rdio.android.sdk.PlayerListener;
import com.rdio.android.sdk.PlayerManager;
import com.rdio.android.sdk.model.Track;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NowPlayingActivity extends AppCompatActivity implements PlayerListener {

    private Lobby mLobby;
    private PlayerManager mPlayerManager;
    private LoadImageViewTask mLoadImageViewTask;

    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mAlbumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLobby = CurrentUser.getInstance().getHostingLobby();
        mPlayerManager = CurrentUser.getInstance().getRdio().getPlayerManager();
        mPlayerManager.addPlayerListener(this);
        mLoadImageViewTask = new LoadImageViewTask();

        mImageView = (ImageView) findViewById(R.id.image_view);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mArtistTextView = (TextView) findViewById(R.id.artist_text_view);
        mAlbumTextView = (TextView) findViewById(R.id.album_text_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NowPlayingActivity.this.onSearchRequested();
            }
        });

        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NowPlayingActivity.this.playPause();
            }
        });

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NowPlayingActivity.this.next();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Track track = SearchActivity.getSelectedTrack();
        SearchActivity.clearSelectedTrack();
        if (track != null) {
            mLobby.getQueue().add(new PlayRequest(track.key));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Player

    private void playPause() {
        if (mPlayerManager.getCurrentTrack() != null) {
            if (mPlayerManager.isPlaying()) {
                mPlayerManager.pause();
            } else {
                mPlayerManager.play();
            }
        } else {
            next();
        }
    }

    private void next() {
        PlayRequest request = mLobby.getQueue().poll();
        if (request != null) {
            mPlayerManager.play(request);
        } else {
            mPlayerManager.stop();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PlayerListener

    public void onPrepared() {

    }

    public void onComplete() {
        mTitleTextView.setText(null);
        mArtistTextView.setText(null);
        mAlbumTextView.setText(null);
        clearImageView();
        next();
    }

    public void onError(AudioError error) {
        mTitleTextView.setText("Error:");
        mArtistTextView.setText(error.getDescription());
        mAlbumTextView.setText(null);
        clearImageView();
    }

    public void onPositionUpdate(int position) {

    }

    public void onPlayStateChanged(PlayerListener.PlayState state) {
        Track track = mPlayerManager.getCurrentTrack();
        if (track != null) {
            mTitleTextView.setText(track.name);
            mArtistTextView.setText(track.artistName);
            mAlbumTextView.setText(track.albumName);
            mLoadImageViewTask.cancel(true);
            mLoadImageViewTask = new LoadImageViewTask();
            mLoadImageViewTask.execute(mImageView, track);
        }
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
    // Image

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
