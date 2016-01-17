package com.geoculturedemo.nickstamp.geoculturedemo.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.geoculturedemo.nickstamp.geoculturedemo.Adapter.MoviesAdapter;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    private static final String ARG_LOCATION = "ARG_LOCATION";
    private static final String SORT_BY_YEAR = "&sort=year,desc";

    private ArrayList<Movie> movies;

    private Location location;

    private ProgressBar pbList;

    private RecyclerView recyclerView;
    private View fragmentView;
    private Context context;
    private MovieParser moviesParser;
    private MoviesAdapter moviesAdapter;
    private OnMovieClicked onMovieClicked;
    private LinearLayoutManager linearLayoutManager;
    private String urlQuery;

    public MovieListFragment() {
        movies = new ArrayList<>();
        moviesAdapter = null;
    }

    public static MovieListFragment newInstance(Location location) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = (Location) getArguments().getSerializable(ARG_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {

            context = getContext();

            fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

            pbList = (ProgressBar) fragmentView.findViewById(R.id.pbList);

            recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerList);
            linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);

            parseMovies();

        }


        return fragmentView;
    }

    private void parseMovies() {
        moviesParser = new MovieParser();
        moviesParser.execute();
    }

    public void setOnMovieClickedListener(OnMovieClicked listener) {
        onMovieClicked = listener;
    }

    public class MovieParser extends AsyncTask<Void, Void, Void> {

        public MovieParser() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            movies.clear();

            recyclerView.setVisibility(View.INVISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (!location.getCity().equals(location.getArea()))
                parseLocation(location.getArea());
            parseLocation(location.getCity());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (moviesAdapter != null) {
            } else {
                moviesAdapter = new MoviesAdapter(context, movies, onMovieClicked);
                recyclerView.swapAdapter(moviesAdapter, true);
            }

            AnimationUtils.crossfade(recyclerView, pbList);

        }

        private void parseLocation(String location) {

            urlQuery = "http://www.imdb.com/search/title?countries=gr&count=100&locations=" + location + SORT_BY_YEAR;

            //try to connect 3 times
            int tries = 0;
            boolean success = false;

            while (tries < 3 && !success) {
                tries++;
                try {
                    // Connect to the web site
                    Document document = Jsoup.connect(urlQuery).get();

                    //if connected successfully
                    success = true;

                    //this movie will be used to locate the header inside the adapter
                    Movie notRealMovie = new Movie("", "0 " + getString(R.string.text_results_for) + " \"" + location + "\"", "", "", "-1", "", "", "", "");

                    //save the pos of the header, to change it later
                    int headerPos = movies.size();
                    movies.add(notRealMovie);

                    int moviesCount = 0;
                    // Using Elements to get the class data
                    Elements tables = document.select("table");
                    Element table = tables.get(0);
                    Elements rows = table.getElementsByTag("tr");
                    for (int i = 1; i < rows.size(); i++) {

                        Element row = rows.get(i);
                        String imgUrl = row.getElementsByClass("image").get(0).getElementsByTag("img").attr("src");

                        Element elementTitle = row.getElementsByClass("title").get(0);
                        String title = elementTitle.getElementsByTag("a").get(0).text();
                        String year = elementTitle.getElementsByClass("year_type").text();
                        String url = elementTitle.getElementsByTag("a").get(0).attr("href");
                        String genre = elementTitle.getElementsByClass("genre").text();
                        String runtime = elementTitle.getElementsByClass("runtime").text();
                        String rating = elementTitle.getElementsByClass("rating-rating").text().split("/")[0];
                        String credit = elementTitle.getElementsByClass("credit").text();

                        //extract actors and director from credits
                        String director = "", cast = "";
                        if (credit.length() > 0) {
                            String[] tokens = credit.split(":");
                            if (tokens.length == 3) {
                                director = tokens[1].subSequence(0, tokens[1].length() - 4).toString();
                                cast = tokens[2];
                            } else {
                                if (tokens[0].equalsIgnoreCase("dir"))
                                    cast = tokens[1];
                                else
                                    director = tokens[1];
                            }
                        }

                        Movie movie = new Movie(url, title, rating, imgUrl, runtime, genre, director, cast, year);

                        if (!movies.contains(movie)) {
                            movies.add(movie);
                            moviesCount++;
                        }

                    }

                    movies.get(headerPos).setTitle(moviesCount + " " + getString(R.string.text_results_for) + " \"" + location + "\"");

                } catch (IOException e) {
                    Log.i("nikos", "IO Exception");
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    Log.i("nikos", "Out of bounds Exception");
                    e.printStackTrace();
                }
            }
            if (!success) {
                Snackbar.make(fragmentView, getString(R.string.snackbar_went_wrong), Snackbar.LENGTH_LONG).show();
            }

        }


    }

}
