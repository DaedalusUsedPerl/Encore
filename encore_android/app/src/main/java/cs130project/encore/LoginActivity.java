package cs130project.encore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import com.rdio.android.sdk.activity.OAuth2WebViewActivity;

public class LoginActivity extends AppCompatActivity implements CurrentUserListener {
    private static final int AUTH_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CurrentUser.setListener(this);
        CurrentUser.getInstance();
    }

    public void onLoggedIn(boolean loggedIn) {
        if (loggedIn) {
            enterLobby();
        }
    }

    public void login(View view) {
        enterLobby();
    }

    public void rdioLogin(View view) {
        Intent intent = CurrentUser.getInstance().getRdioLoginIntent(this);
        startActivityForResult(intent, AUTH_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CurrentUser.getInstance().handleRdioLoginIntentResult(data);
            } else if (resultCode == RESULT_CANCELED) {
                if (data == null) {
                    Log.v("Login", "User decided not to auth with the app");
                } else {
                    String error = data.getStringExtra(OAuth2WebViewActivity.EXTRA_ERROR);
                    String errorDesc = data.getStringExtra(OAuth2WebViewActivity.EXTRA_ERROR_DESCRIPTION);
                    Log.e("Login", "There was a problem when authorizing user: " + error + " - " + errorDesc);
                }
            }
        }
    }

    public void enterLobby() {
        Intent intent = new Intent(this, FindOrCreateLobby.class);
        startActivity(intent);
    }
}
