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

import com.geoculturedemo.nickstamp.geoculturedemo.Adapter.SongsAdapter;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongClicked;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.LocationUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends Fragment {

    private static final String ARG_LOCATION = "ARG_LOCATION";

    private ArrayList<Song> songs;

    private Location location;

    private ProgressBar pbList;

    private RecyclerView recyclerView;
    private View fragmentView;
    private Context context;
    private SongParser songParser;
    private SongsAdapter songsAdapter;
    private OnSongClicked onSongClicked;
    private LinearLayoutManager linearLayoutManager;
    private String urlQuery;

    private String sResults;


    public SongListFragment() {
        songs = new ArrayList<>();
    }

    public static SongListFragment newInstance(Location location) {
        SongListFragment fragment = new SongListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = new LocationUtils(getContext()).toGreekLocale((Location) getArguments().getSerializable(ARG_LOCATION));
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

            sResults = getString(R.string.text_results_for);

            parseSongs();


        }

        return fragmentView;
    }

    private void parseSongs() {

        songParser = new SongParser();
        songParser.execute();
    }

    public void setOnSongClicked(OnSongClicked listener) {
        onSongClicked = listener;
    }

    public void shutDownAsyncTask() {
        songParser.cancel(true);
    }

    public class SongParser extends AsyncTask<Void, Void, Void> {

        public SongParser() {


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            songs.clear();

            recyclerView.setVisibility(View.INVISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (isCancelled())
                return null;

            if (location != null && !location.getCity().equals(location.getArea()))
                parseSongList(location.getArea());
            parseSongList(location.getCity());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (songsAdapter != null) {
            } else {
                songsAdapter = new SongsAdapter(context, songs, onSongClicked);
                recyclerView.swapAdapter(songsAdapter, true);
            }

            AnimationUtils.crossfade(recyclerView, pbList);

        }
        private void parseSongList(String location) {

            urlQuery = "http://www.stixoi.info/stixoi.php?keywords=" + location + "&act=ss&info=SS";

            //try to connect 3 times
            int tries = 0;
            boolean success = false;

            while (tries < 3 && !success) {
                tries++;
                try {
                    // Connect to the web site
                    Document document = Jsoup.connect(urlQuery).get();

                    success = true;

                    if(isCancelled())
                        return;

                    //this song will be used to locate the header inside the adapter
                    Song notRealSong = new Song("", "0 " + sResults + " \"" + location + "\"", "-1", "", "", "", "");
                    //save the pos of the header, to change it later
                    int headerPos = songs.size();
                    songs.add(notRealSong);

                    int songCount = 0;

                    Elements elements = document.select("table");

                    Element table = elements.get(5);

                    Elements rows = table.getElementsByTag("tr");
                    for (int currentRow = 1; currentRow < rows.size(); currentRow++) {

                        Element row = rows.get(currentRow);
                        Elements rowElements = row.getElementsByTag("td");

                        String id = rowElements.get(0).text();
                        String title = rowElements.get(2).text();
                        String url = rowElements.get(2).getElementsByTag("a").attr("href");
                        String lyricist = rowElements.get(3).text();
                        String composer = rowElements.get(4).text();
                        String singer = rowElements.get(5).text();
                        String year = rowElements.get(6).text();

                        Song song = new Song(id, title, year, lyricist, composer, singer, url);

                        if (!songs.contains(song)) {
                            songs.add(song);
                            songCount++;
                        }

                    }


                    songs.get(headerPos).setTitle(songCount + " " + sResults + " \"" + location + "\"");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            if (!success) {
                Snackbar.make(fragmentView, getString(R.string.snackbar_went_wrong), Snackbar.LENGTH_LONG).show();
            }

        }

    }

}
