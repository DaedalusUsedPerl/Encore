package cs130project.encore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        TextView nameView = (TextView)findViewById(R.id.logindisplay);
        nameView.setText("Logged in as " + CurrentUser.getInstance().getName());


    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, FindOrCreateLobby.class);
        startActivity(intent);
    }

}
