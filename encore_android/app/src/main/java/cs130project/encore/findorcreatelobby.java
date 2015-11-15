package cs130project.encore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FindOrCreateLobby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findorcreatelobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Available Lobbies");

    }

    /**
     * Add a button to the ScrollView to allow the user to go to a lobby
     * @param o An object specifying the lobby
     */
    private void addButtonForLobby(Object o){
        final LinearLayout container = (LinearLayout)findViewById(R.id.findLobbiesButtonList);
        final Button newButton = new Button(this);
        newButton.setWidth(50);
        newButton.setHeight(20);
        newButton.setText("Test Button");
        container.post(new Runnable(){
            public void run(){
                container.addView(newButton);
            }
        });
    }

    public void enterLobbyMaker(View view) {
        Intent intent = new Intent(this, LobbySettings.class);
        startActivity(intent);
    }

}
