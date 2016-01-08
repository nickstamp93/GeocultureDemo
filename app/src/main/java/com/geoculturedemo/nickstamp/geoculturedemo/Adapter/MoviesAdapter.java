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

        View view = inflater.inflate(R.layout.list_item_movie, parent, false);

        return new MoviesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesViewHolder holder, int position) {

        Movie movie = movies.get(position);

        holder.movieTitle.setText(movie.getTitle());
        holder.movieGenre.setText(movie.getGenre());
        holder.movieRuntime.setText(movie.getRuntime());
        holder.movieRating.setText(movie.getRating());
        Picasso.with(context).load(movies.get(position).getImgUrl())
                .resize(54, 74)
                .centerInside()
                .into(holder.movieImage);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movieImage;
        TextView movieTitle, movieGenre, movieRating, movieRuntime;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            movieImage = (ImageView) itemView.findViewById(R.id.ivMovieImage);
            movieTitle = (TextView) itemView.findViewById(R.id.tvMovieTitle);
            movieGenre = (TextView) itemView.findViewById(R.id.tvMovieGenre);
            movieRating = (TextView) itemView.findViewById(R.id.tvMovieRating);
            movieRuntime = (TextView) itemView.findViewById(R.id.tvMovieRuntime);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieClicked.onMovie(movies.get(getAdapterPosition()));
        }
    }
}
