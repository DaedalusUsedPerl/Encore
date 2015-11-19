package cs130project.encore;

import com.rdio.android.sdk.PlayRequest;

import java.util.LinkedList;
import java.util.Queue;

public class Lobby {
    private Queue<PlayRequest> mQueue = new LinkedList<PlayRequest>();

    public Queue<PlayRequest> getQueue() {
        return mQueue;
    }
}
