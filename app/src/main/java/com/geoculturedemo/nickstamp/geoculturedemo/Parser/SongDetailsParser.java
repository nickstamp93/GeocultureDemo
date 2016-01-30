package com.geoculturedemo.nickstamp.geoculturedemo.Parser;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnSongDetailsDownloaded;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Song;
import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.geoculturedemo.nickstamp.geoculturedemo.Utils.AnimationUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nickstamp on 1/30/2016.
 */
public class SongDetailsParser extends AsyncTask<Void, Void, Void> {

    private final Song song;
    private final OnSongDetailsDownloaded onSongDetailsDownloaded;
    private final ArrayList<String> links;
    private final ArrayList<String> artists;
    private String lyrics;

    public SongDetailsParser(Song song, OnSongDetailsDownloaded onSongDetailsDownloaded) {
        this.song = song;
        this.onSongDetailsDownloaded = onSongDetailsDownloaded;

        artists = new ArrayList<>();
        links = new ArrayList<>();
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
                Document document = Jsoup.connect(song.getUrl()).get();

                Element table = document.select(".row3").get(0);

                Element singerElement = table.getElementsByClass("singers").get(0);
                Elements trSingers = singerElement.getElementsByTag("tr");
                for (Element e : trSingers) {
                    String singer = e.text();
                    artists.add(singer);
                    for (int i = 0; i < e.getElementsByTag("td").size(); i++) {
                        String s = e.getElementsByTag("td").get(i).getElementsByTag("a").attr("href");
                        if (s.contains("youtube")) {
                            links.add(s);
                            break;
                        }
                    }
                    if (artists.size() > links.size())
                        links.add("-");
                }

                StringBuilder sbArtists = new StringBuilder(), sbLinks = new StringBuilder();
                for (String s : artists) {
                    sbArtists.append(s).append("|");
                }
                for (String s : links) {
                    sbLinks.append(s).append("|");
                }
                sbArtists.deleteCharAt(sbArtists.length() - 1);
                sbLinks.deleteCharAt(sbLinks.length() - 1);
                song.setArtist(sbArtists.toString());
                song.setLinks(sbLinks.toString());

                Elements divs = table.getElementsByTag("div");
                divs.remove();

                Elements b = table.getElementsByTag("b");
                if (b != null)
                    b.remove();

                lyrics = Html.fromHtml(table.html()).toString();
                song.setLyrics(lyrics);

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

        onSongDetailsDownloaded.onDownload(song);

    }

}