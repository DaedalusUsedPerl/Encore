package cs130project.encore;

import android.app.Application;
import android.content.Context;

/**
 * Top level application class.
 * Created by steven on 10/28/15.
 */
public class App extends Application {
    private static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}