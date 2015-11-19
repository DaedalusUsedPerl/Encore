package cs130project.encore;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rdio.android.core.RdioApiResponse;
import com.rdio.android.core.RdioService_Api;
import com.rdio.android.sdk.PlayRequest;
import com.rdio.android.sdk.model.Track;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements RdioService_Api.ResponseListener, AdapterView.OnItemClickListener {

    private static Track mSelectedTrack;
    private TextView mHeaderTextView;
    private SearchAdapter mListAdapter;
    private ListView mListView;
    final private List<Track> mTracks = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);

        View headerView = (View)getLayoutInflater().inflate(R.layout.search_header, mListView, false);
        mHeaderTextView = (TextView) headerView.findViewById(R.id.text_view);
        mHeaderTextView.setText("Loading...");
        mListView.addHeaderView(headerView, null, false);

        mListAdapter = new SearchAdapter(this, R.layout.search_item, mTracks);
        mListView.setAdapter(mListAdapter);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Query

    private void search(String query) {
        ArrayList<String> types = new ArrayList<String>();
        types.add("Track");
        ArrayList<String> extras = new ArrayList<String>();
        extras.add("*");
        CurrentUser.getInstance().getRdioService().search(query, types, false, 0, 10, extras, false, null, this);
    }

    public void onResponse(RdioApiResponse response) {
        if (response.isSuccess()) {
            JSONObject result = response.getResult();

            mListAdapter.clear();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Track>>(){}.getType();
            List<Track> list;
            try {
                list = gson.fromJson(result.getJSONArray("results").toString(), listType);
                mListAdapter.addAll(list);
            } catch (JSONException e) {
                list = null;
                e.printStackTrace();
            }

            if (list == null || list.size() == 0) {
                mHeaderTextView.setText("No results");
                mHeaderTextView.setVisibility(View.VISIBLE);
            } else {
                mHeaderTextView.setVisibility(View.GONE);
            }
        } else {
            Log.e("error", response.getErrorMessage());

            mHeaderTextView.setText("Error: " + response.getErrorMessage());
            mHeaderTextView.setVisibility(View.VISIBLE);
        }
    }

    private void select() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // List

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int cellPosition, long id) {
        final int position = cellPosition - 1;
        mSelectedTrack = mTracks.get(position);
        finish();
    }

    public static Track getSelectedTrack() {
        return mSelectedTrack;
    }

    public static void clearSelectedTrack() {
        mSelectedTrack = null;
    }

}
