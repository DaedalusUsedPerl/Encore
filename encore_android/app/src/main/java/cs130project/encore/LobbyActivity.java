package cs130project.encore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LobbyActivity extends AppCompatActivity {

    private static class SongData{
        //The name of the song
        public String name;
        //The number of votes accumalated
        public int votes;
    }

    private void addSong(SongData data){
        final ListView items = (ListView)findViewById(R.id.songVoteList);
        final TextView songName = new TextView(this);
        songName.setText(data.name + ": " + data.votes);
        final Button upvote = new Button(this);
        upvote.setText("Upvote");
        upvote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Call the API to upvote this song
            }
        });
        final Button downvote = new Button(this);
        downvote.setText("Downvote");
        downvote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Call the API to upvote this song
            }
        });
        items.post(new Runnable(){
            public void run(){
                //Preferably, add these to a single container so they can be kept in one row
                //Unfortunately, I don't know how to do that
                //So now they just get added one below the other
                items.addView(songName);
                items.addView(upvote);
                items.addView(downvote);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent createdBy = getIntent();
        setTitle(createdBy.getStringExtra("Lobby Name"));
        int id = createdBy.getIntExtra("Lobby ID", -1);
        if(id != -1) {
            //Start the service to pull songs for the lobby
        }
    }

}
