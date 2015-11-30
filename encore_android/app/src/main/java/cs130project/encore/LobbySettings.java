package cs130project.encore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LobbySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lobby Settings");

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView nameView = (TextView)findViewById(R.id.edit_name);
        nameView.setText("Logged in as " + CurrentUser.getInstance().getName());
    }

    public void saveLobby(View view) {
        TextView nameView = (TextView)findViewById(R.id.edit_name);
        final Button submitButton = (Button)findViewById(R.id.makelobby);

        submitButton.setEnabled(false);
        new Lobby(nameView.getText().toString(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) { // success
                    Lobby lobby = (Lobby) msg.obj;
                    Intent intent = getIntent();
                    intent.putExtra("lobbyId", lobby.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else { // failure
                    submitButton.setEnabled(true);
                }
                return true;
            }
        });
    }

}
