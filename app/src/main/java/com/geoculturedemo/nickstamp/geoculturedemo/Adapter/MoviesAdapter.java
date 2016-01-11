package com.geoculturedemo.nickstamp.geoculturedemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nickstamp on 1/8/2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {


    private static final int VIEW_TYPE_MOVIE = 1;
    private static final int VIEW_TYPE_HEADER = 0;


    private Context context;
    private ArrayList<Movie> movies;
    private OnMovieClicked onMovieClicked;
    private LayoutInflater inflater;

    public MoviesAdapter(Context context, ArrayList<Movie> movies, OnMovieClicked onMovieClicked) {
        this.context = context;
        this.movies = movies;
        this.onMovieClicked = onMovieClicked;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.list_item_header, parent, false);

            return new MoviesViewHolder(view, VIEW_TYPE_HEADER);
        } else {
            View view = inflater.inflate(R.layout.list_item_movie, parent, false);

            return new MoviesViewHolder(view, VIEW_TYPE_MOVIE);
        }

    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesViewHolder holder, int position) {

        if (holder.type == VIEW_TYPE_MOVIE) {
            Movie movie = movies.get(position);

            holder.movieTitle.setText(movie.getTitle() + " " + movie.getYear());
            holder.movieGenre.setText(movie.getGenre());
            holder.movieRuntime.setText(movie.getRuntime());
            holder.movieRating.setText(movie.getRating());
            Picasso.with(context).load(movies.get(position).getImgUrl())
                    .resize(54, 74)
                    .centerInside()
                    .into(holder.movieImage);
        } else {
            holder.tvHeaderResults.setText(movies.get(position).getTitle());
        }


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getRuntime().equals("-1"))
            return VIEW_TYPE_HEADER;
        else
            return VIEW_TYPE_MOVIE;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int type;

        ImageView movieImage;
        TextView movieTitle, movieGenre, movieRating, movieRuntime;

        TextView tvHeaderResults;

        public MoviesViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_MOVIE) {
                type = VIEW_TYPE_MOVIE;
                movieImage = (ImageView) itemView.findViewById(R.id.ivMovieImage);
                movieTitle = (TextView) itemView.findViewById(R.id.tvMovieTitle);
                movieGenre = (TextView) itemView.findViewById(R.id.tvMovieGenre);
                movieRating = (TextView) itemView.findViewById(R.id.tvMovieRating);
                movieRuntime = (TextView) itemView.findViewById(R.id.tvMovieRuntime);

                itemView.setOnClickListener(this);
            } else {
                type = VIEW_TYPE_HEADER;
                tvHeaderResults = (TextView) itemView.findViewById(R.id.tvHeader);
            }

        }

        @Override
        public void onClick(View v) {
            onMovieClicked.onMovie(movies.get(getAdapterPosition()));
        }
    }
}
