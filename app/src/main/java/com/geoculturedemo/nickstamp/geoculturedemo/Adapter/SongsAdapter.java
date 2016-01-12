package com.geoculturedemo.nickstamp.geoculturedemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import java.util.ArrayList;

/**
 * Created by nickstamp on 1/11/2016.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MoviesViewHolder> {


    private static final int VIEW_TYPE_MOVIE = 1;
    private static final int VIEW_TYPE_HEADER = 0;


    private Context context;
    private ArrayList<Song> songs;
    private OnSongClicked onSongClicked;
    private LayoutInflater inflater;

    public SongsAdapter(Context context, ArrayList<Song> songs, OnSongClicked onSongClicked) {
        this.context = context;
        this.songs = songs;
        this.onSongClicked = onSongClicked;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public SongsAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.list_item_header, parent, false);

            return new MoviesViewHolder(view, VIEW_TYPE_HEADER);
        } else {
            View view = inflater.inflate(R.layout.list_item_song, parent, false);

            return new MoviesViewHolder(view, VIEW_TYPE_MOVIE);
        }

    }

    @Override
    public void onBindViewHolder(SongsAdapter.MoviesViewHolder holder, int position) {

        if (holder.type == VIEW_TYPE_MOVIE) {

            Song song = songs.get(position);

            holder.songTitle.setText(song.getId() + "." + song.getTitle());
            holder.songArtist.setText(song.getArtist());

        } else {
            holder.tvHeaderResults.setText(songs.get(position).getTitle());
        }

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (songs.get(position).getYear().equals("-1"))
            return VIEW_TYPE_HEADER;
        else
            return VIEW_TYPE_MOVIE;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int type;


        TextView songTitle, songArtist;

        TextView tvHeaderResults;

        public MoviesViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_MOVIE) {
                type = VIEW_TYPE_MOVIE;

                songTitle = (TextView) itemView.findViewById(R.id.tvSongTitle);
                songArtist = (TextView) itemView.findViewById(R.id.tvSongArtist);

                itemView.setOnClickListener(this);
            } else {
                type = VIEW_TYPE_HEADER;
                tvHeaderResults = (TextView) itemView.findViewById(R.id.tvHeader);
            }

        }

        @Override
        public void onClick(View v) {

        }
    }
}