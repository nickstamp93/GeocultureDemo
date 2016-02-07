package com.geoculturedemo.nickstamp.geoculturedemo.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by nickstamp on 1/29/2016.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {


    public static final int VIEW_TYPE_MOVIE = 1;
    public static final int VIEW_TYPE_SONG = 2;
    public static final int VIEW_TYPE_HEADER = 0;


    private Context context;
    private ArrayList<Object> objects;
    private OnMovieClicked onMovieClicked;
    private OnSongClicked onSongClicked;
    private LayoutInflater inflater;
    Typeface typeface, typefaceMedium, typefaceBold;

    public FavoritesAdapter(Context context, ArrayList<Object> list, OnMovieClicked onMovieClicked, OnSongClicked onSongClicked) {
        this.context = context;
        this.objects = list;
        this.onMovieClicked = onMovieClicked;
        this.onSongClicked = onSongClicked;

        inflater = LayoutInflater.from(context);

        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        typefaceMedium = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
    }

    @Override
    public FavoritesAdapter.FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.list_item_header, parent, false);

            return new FavoritesViewHolder(view, VIEW_TYPE_HEADER);
        } else if (viewType == VIEW_TYPE_MOVIE) {
            View view = inflater.inflate(R.layout.grid_item_movie, parent, false);

            return new FavoritesViewHolder(view, VIEW_TYPE_MOVIE);
        } else {

            View view = inflater.inflate(R.layout.list_item_song, parent, false);

            return new FavoritesViewHolder(view, VIEW_TYPE_SONG);

        }

    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.FavoritesViewHolder holder, int position) {
        if (holder.type == VIEW_TYPE_MOVIE) {
            Movie movie = ((Movie) objects.get(position));

            holder.movieTitle.setText(movie.getTitle());
            Picasso.with(context)
                    .load(new File(movie.getImgUrl()))
                    .into(holder.movieImage);
        } else if (holder.type == VIEW_TYPE_HEADER) {
            holder.tvHeaderResults.setText(((String) objects.get(position)));
        } else {

            Song song = ((Song) objects.get(position));

            holder.songTitle.setText(position + "." + song.getTitle());
            if (song.getArtist().equals("Άγνωστος"))
                holder.songArtist.setText("Άγνωστος");
            else
                holder.songArtist.setText(song.getArtist().split("\\|")[0].split("\\.")[1]);

        }

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (objects.get(position) instanceof Movie)
            return VIEW_TYPE_MOVIE;
        else if (objects.get(position) instanceof Song)
            return VIEW_TYPE_SONG;
        else
            return VIEW_TYPE_HEADER;
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int type;

        ImageView movieImage;
        TextView movieTitle, songTitle, songArtist;

        TextView tvHeaderResults;

        public FavoritesViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_MOVIE) {
                type = VIEW_TYPE_MOVIE;
                movieImage = (ImageView) itemView.findViewById(R.id.ivMovieImage);
                movieTitle = (TextView) itemView.findViewById(R.id.tvMovieTitle);

                movieTitle.setTypeface(typefaceMedium);

                itemView.setOnClickListener(this);
            } else if (viewType == VIEW_TYPE_HEADER) {
                type = VIEW_TYPE_HEADER;
                tvHeaderResults = (TextView) itemView.findViewById(R.id.tvHeader);
                tvHeaderResults.setTypeface(typefaceBold);
            } else {

                type = VIEW_TYPE_SONG;
                songTitle = (TextView) itemView.findViewById(R.id.tvSongTitle);
                songArtist = (TextView) itemView.findViewById(R.id.tvSongArtist);

                songTitle.setTypeface(typefaceMedium);
                songArtist.setTypeface(typeface);

                itemView.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if (objects.get(getAdapterPosition()) instanceof Movie)
                onMovieClicked.onMovie(((Movie) objects.get(getAdapterPosition())), movieImage);
            else
                onSongClicked.onSong(((Song) objects.get(getAdapterPosition())));
        }
    }
}