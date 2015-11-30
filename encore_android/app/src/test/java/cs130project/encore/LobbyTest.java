package cs130project.encore;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Created by Shubham on 11/30/2015.
 */
public class LobbyTest {

    @Test
    public void testUpdate() throws Exception {
        String santaBaby = "{" +
                "\"id\": \"1\"," +
                "\"title\": \"Santa Baby\"," +
                "\"artist\": \"Eartha Kitt\"," +
                "\"vote_count\": 15," +
                "\"lobby_id\": \"1\"," +
                "\"rdio_id\": \"1\"," +
                "}";
        String beEvil = "{" +
                "\"id\": \"2\"," +
                "\"title\": \"I Want to be Evil\"," +
                "\"artist\": \"Eartha Kitt\"," +
                "\"vote_count\": 15," +
                "\"lobby_id\": \"1\"," +
                "\"rdio_id\": \"2\"," +
                "}";
        JSONObject lobbyJSON = new JSONObject("{" +
                "\"id\": \"1\"," +
                "\"name\": TestLobby," +
                "\"queued_songs\": [" + santaBaby + ", " + beEvil +  "]" +
                "}");
        Lobby l = new Lobby(lobbyJSON);//Constructor calls the update function
        assertTrue(l.getId() == "1");
        assertTrue("TestLobby".equals(l.getName()));
        Queue<Song> queue = l.getQueue();
        assertTrue(queue.containsAll(Arrays.asList(
                new Song(new JSONObject(santaBaby)),
                new Song(new JSONObject(beEvil)))));
    }
}