package cs130project.encore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.rdio.android.core.RdioApiResponse;
import com.rdio.android.sdk.OAuth2Credential;
import com.rdio.android.sdk.PlayRequest;
import com.rdio.android.sdk.Rdio;
import com.rdio.android.sdk.RdioListener;
import com.rdio.android.sdk.RdioResponseListener;
import com.rdio.android.sdk.RdioService;
import com.rdio.android.sdk.activity.OAuth2WebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class CurrentUser implements RdioListener {

    final private static String CLIENT_ID = "gn5qyos7cfdejk25q2yldfx6gq";
    final private static String CLIENT_SECRET = "lpRVH8U3QJz0jNS4PC_UlQ";
    final private static String REDIRECT_URI = "https://github.com/DaedalusUsedPerl/Encore";

    private static CurrentUser instance;
    private static CurrentUserListener listener;

    private String name;
    private Lobby mHostingLobby = new Lobby();

    private Rdio rdio;
    private RdioService rdioService;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Object

    private CurrentUser() {
        SharedPreferences pref = App.getContext().getSharedPreferences("rdio", Context.MODE_PRIVATE);
        OAuth2Credential credential = null;
        if (pref.contains("saved_credential_access")) {
            credential = new OAuth2Credential(
                    pref.getString("saved_credential_access", null),
                    pref.getString("saved_credential_refresh", null),
                    pref.getLong("saved_credential_time", 0));
        } else {
            listener.onLoggedIn(false);
        }
        rdio = new Rdio(CLIENT_ID, CLIENT_SECRET, credential, App.getContext(), this);
        rdio.requestApiService();
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            synchronized (CurrentUser.class) {
                if (instance == null) {
                    instance = new CurrentUser();
                }
            }
        }
        return instance;
    }

    public static void setListener(CurrentUserListener l) {
        listener = l;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // User

    public String getName() {
        return name;
    }

    public Lobby getHostingLobby() {
        return mHostingLobby;
    }

    public void setHostingLobby(Lobby lobby) {
        mHostingLobby = lobby;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Rdio

    public Rdio getRdio() {
        return rdio;
    }

    public RdioService getRdioService() {
        return rdioService;
    }

    public Intent getRdioLoginIntent(Activity from) {
        Intent intent = new Intent(from, OAuth2WebViewActivity.class);
        try {
            intent.putExtra(OAuth2WebViewActivity.EXTRA_AUTH_URL, rdio.getAuthUrl(REDIRECT_URI));
            intent.putExtra(OAuth2WebViewActivity.EXTRA_REDIRECT_URI, REDIRECT_URI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intent;
    }

    public void handleRdioLoginIntentResult(Intent data) {
        rdioService.processWebViewActivity(data, REDIRECT_URI);
    }

    @Override
    public void onRdioAuthorised(OAuth2Credential credential) {
        SharedPreferences pref = App.getContext().getSharedPreferences("rdio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("saved_credential_access", credential.accessToken);
        editor.putString("saved_credential_refresh", credential.refreshToken);
        editor.putLong("saved_credential_time", credential.expirationTimeMSec);
        editor.commit();

        rdio.prepareForPlayback();
        rdioService.currentUser(new RdioResponseListener() {
            @Override
            public void onResponse(RdioApiResponse response) {
                if (response.isSuccess()) {
                    JSONObject user = response.getResult();
                    try {
                        name = user.getString("firstName") + " " + user.getString("lastName");
                        listener.onLoggedIn(true);
                    } catch (JSONException e) {
                        Log.e("CurrentUser", "Authed -- error reading response");
                    }
                } else {
                    Log.e("CurrentUser", "No user currently authorized");
                }
            }
        });
    }

    public void onApiServiceReady(RdioService service) {
        rdioService = service;
    }

    @Override
    public void onRdioReadyForPlayback() {
        Log.i("CurrentUser", "Rdio SDK is ready for playback!!");
    }

    @Override
    public void onRdioUserPlayingElsewhere() {
        Log.i("CurrentUser", "Rdio is now playing elsewhere, our music has been paused");
    }

    @Override
    public void onError(Rdio.RdioError error, String message) {
        Log.e("CurrentUser", "Oh no, we just got an error : " + error + " w/ msg " + message);
    }

}
