package com.geoculturedemo.nickstamp.geoculturedemo.Parser;

import android.os.AsyncTask;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnMovieDetailsDownloaded;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Movie;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by nickstamp on 1/30/2016.
 */
public class MovieDetailsParser extends AsyncTask<Void, Void, Void> {

    private Movie movie;
    private OnMovieDetailsDownloaded onMovieDetailsDownloaded;

    public MovieDetailsParser(Movie movie, OnMovieDetailsDownloaded onMovieDetailsDownloaded) {

        this.movie = movie;
        this.onMovieDetailsDownloaded = onMovieDetailsDownloaded;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {

        //try to connect 3 times
        int tries = 0;
        boolean success = false;

        while (tries < 3 && !success) {
            tries++;

            try {
                // Connect to the web site
                Document document = Jsoup.connect(movie.getUrl()).get();

                /*Element titleSection = document.select("div.titleBar").get(0);

                Element titleWrapper = titleSection.getElementsByClass("title_wrapper").get(0);
                String title = titleWrapper.getElementsByTag("h1").text();
                movie.setTitle(title);*/

                String imgUrl = document.select("div.poster").get(0).getElementsByTag("img").attr("src");
                movie.setImgUrl(imgUrl);

                String creditWriter = document.select(".credit_summary_item").get(1).text();
                movie.setWriter(creditWriter);

                Element plotSummary = document.select("div.plot_summary").get(0);
                Elements writers = plotSummary.getElementsByAttributeValue("itemprop", "creator").select("a[itemprop=url]");
                String writer = "";
                for (Element w : writers) {
                    writer += w.text() + " , ";
                }
                movie.setWriter(writer.substring(0, writer.length() - 3));

                Elements storylines = document.select("#titleStoryLine");
                if (storylines.size() > 0) {

                    String synopsis = storylines.first().getElementsByTag("p").text();
                    int pos = synopsis.indexOf("Written by");
                    synopsis = synopsis.substring(0, pos);
                    movie.setSynopsis(synopsis);
                }

                success = true;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        onMovieDetailsDownloaded.onDownload(movie);

    }

}