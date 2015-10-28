package cs130project.encore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class findorcreatelobby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findorcreatelobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView nameView = (TextView)findViewById(R.id.nameView);
        nameView.setText("Hello " + CurrentUser.getInstance().getName());
    }

    public void enterLobby(View view) {
        Intent intent = new Intent(this, LobbySettings.class);
        startActivity(intent);
    }

}
