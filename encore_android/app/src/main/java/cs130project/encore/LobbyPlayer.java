package cs130project.encore;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.rdio.android.audioplayer.interfaces.AudioError;
import com.rdio.android.sdk.OAuth2Credential;
import com.rdio.android.sdk.PlayerListener;
import com.rdio.android.sdk.PlayerManager;
import com.rdio.android.sdk.Rdio;
import com.rdio.android.sdk.model.Track;

public class LobbyPlayer implements PlayerListener {

    private static LobbyPlayer mInstance;

    private PlayerManager mPlayerManager;
    private Lobby mLobby = null;
    private Song mCurrentSong = null;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Object

    private LobbyPlayer() {
        mPlayerManager = CurrentUser.getInstance().getRdio().getPlayerManager();
        mPlayerManager.addPlayerListener(this);
    }

    public static LobbyPlayer getInstance() {
        if (mInstance == null) {
            synchronized (LobbyPlayer.class) {
                if (mInstance == null) {
                    mInstance = new LobbyPlayer();
                }
            }
        }
        return mInstance;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Lobby

    public Lobby getLobby() {
        return mLobby;
    }

    public void setLobby(Lobby lobby) {
        mLobby = lobby;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Player

    public boolean isReady() {
        if (mLobby == null) {
            return false;
        }
        switch (mPlayerManager.getState()) {
            case Error:
            case None:
            case Stopped:
                return mLobby.getQueue().size() > 1;
            default:
                return true;
        }
    }

    public boolean isPlaying() {
        return mCurrentSong != null && mPlayerManager.getState() == PlayState.Playing || mPlayerManager.isPlaying();
    }

    public Song getCurrentSong() {
        return mCurrentSong;
    }

    public Track getCurrentTrack() {
        return mCurrentSong == null ? null : mPlayerManager.getCurrentTrack();
    }

    public void playPause() {
        if (getCurrentTrack() == null) {
            next();
        } else {
            if (isPlaying()) {
                mPlayerManager.pause();
            } else {
                mPlayerManager.play();
            }
        }
    }

    public void next() {
        if (mCurrentSong != null) {
            mCurrentSong.markPlayed();
        }

        mCurrentSong = mLobby.getQueue().poll();
        if (mCurrentSong != null) {
            mPlayerManager.play(mCurrentSong.getPlayRequest());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PlayerListener

    public void onPrepared() {
    }

    public void onComplete() {
        next();
    }

    public void onError(AudioError error) {
    }

    public void onPositionUpdate(int position) {
    }

    public void onPlayStateChanged(PlayerListener.PlayState state) {
    }

    public void onBufferingStarted() {
    }

    public void onBufferingEnded() {
    }

    public void onSeekStarted() {
    }

    public void onSeekCompleted() {
    }

}
