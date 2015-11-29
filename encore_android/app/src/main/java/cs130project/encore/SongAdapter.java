package cs130project.encore;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    private int mResourceId;
    private LayoutInflater inflater;
    private Context context;
    private Handler.Callback mVoteUpdatedCallback;

    public SongAdapter(Context ctx, int resourceId, List objects) {
        super(ctx, resourceId, objects);
        mResourceId = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        mVoteUpdatedCallback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message _) {
                SongAdapter.this.notifyDataSetChanged();
                return true;
            }
        };
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        final Song song = (Song) getItem(position);

        // Get or create cached trackView
        final SongItemHolder holder;
        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(mResourceId, null);
            holder = new SongItemHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (SongItemHolder) convertView.getTag();
        }

        // Fill data
        holder.getTitleTextView().setText(song.getTitle());
        holder.getArtistTextView().setText(song.getArtist());
        holder.getUpvoteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song.getCurrentUserVoteCount() == 1) {
                    song.clearVote(mVoteUpdatedCallback);
                } else {
                    song.voteUp(mVoteUpdatedCallback);
                }
                updateVoteButtons(holder, song);
            }
        });
        holder.getDownvoteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song.getCurrentUserVoteCount() == -1) {
                    song.clearVote(mVoteUpdatedCallback);
                } else {
                    song.voteDown(mVoteUpdatedCallback);
                }
                updateVoteButtons(holder, song);
            }
        });
        updateVoteButtons(holder, song);

        return convertView;
    }

    private void updateVoteButtons(SongItemHolder holder, Song song) {
        holder.getVoteCountTextView().setText(String.format("%d", song.getVoteCount()));
        // TODO: style buttons
        switch (song.getCurrentUserVoteCount()) {
            case -1:
                holder.getDownvoteButton().setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                holder.getUpvoteButton().setImageTintMode(PorterDuff.Mode.DST);
                break;
            case 0:
                holder.getDownvoteButton().setImageTintMode(PorterDuff.Mode.DST);
                holder.getUpvoteButton().setImageTintMode(PorterDuff.Mode.DST);
                break;
            case 1:
                holder.getDownvoteButton().setImageTintMode(PorterDuff.Mode.DST);
                holder.getUpvoteButton().setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }


}