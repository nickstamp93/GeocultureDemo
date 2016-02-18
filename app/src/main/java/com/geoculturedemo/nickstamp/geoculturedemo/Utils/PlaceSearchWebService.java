package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnPlaceSearchFound;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created by nickstamp on 2/18/2016.
 */
public class PlaceSearchWebService extends AsyncTask<String, Void, PlaceSearchWebService.GPSCoordinates> {

    private Context context;
    private OnPlaceSearchFound onPlaceSearchFound;


    public PlaceSearchWebService(Context context, OnPlaceSearchFound onPlaceSearchFound) {
        this.context = context;
        this.onPlaceSearchFound = onPlaceSearchFound;
    }

    @Override
    protected GPSCoordinates doInBackground(String... params) {
        String query = params[0];

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonResponse = null;

        String language = Locale.getDefault().getLanguage();

        try {
            final String BASE_URL =
                    "https://maps.googleapis.com/maps/api/place/textsearch/json?";
            final String PARAM_QUERY = "query";
            final String PARAM_LANG = "language";
            final String PARAM_APIKEY = "key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, query)
                    .appendQueryParameter(PARAM_LANG, language)
                    .appendQueryParameter(PARAM_APIKEY, context.getString(R.string.API_KEY))
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonResponse = buffer.toString();
        } catch (IOException e) {
            Log.e("nikos", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("nikos", "Error closing stream", e);
                }
            }
        }

        try {
            return getDataFromJson(jsonResponse);
        } catch (JSONException e) {
            Log.e("nikos", e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the response.
        return null;
    }

    private GPSCoordinates getDataFromJson(String jsonResponse) throws JSONException {

        final String TAG_RESULTS = "results";
        final String TAG_GEOMETRY = "geometry";
        final String TAG_LOCATION = "location";
        final String TAG_LAT = "lat";
        final String TAG_LONG = "lng";

        JSONObject response = new JSONObject(jsonResponse);

        if (response.getString("status").equalsIgnoreCase("ok")) {

            JSONObject result = response.getJSONArray(TAG_RESULTS).getJSONObject(0);
            JSONObject geometry = result.getJSONObject(TAG_GEOMETRY);
            JSONObject location = geometry.getJSONObject(TAG_LOCATION);
            double lat = location.getDouble(TAG_LAT);
            double lng = location.getDouble(TAG_LONG);

            return new GPSCoordinates(lat, lng);
        } else return new GPSCoordinates(0, 0);

    }

    @Override
    protected void onPostExecute(GPSCoordinates gpsCoordinates) {
        onPlaceSearchFound.onPlaceFound(gpsCoordinates.latitude, gpsCoordinates.longitude);
    }

    // consider returning Location instead of this dummy wrapper class
    public static class GPSCoordinates {
        public double longitude = -1;
        public double latitude = -1;

        public GPSCoordinates(double theLatitude, double theLongitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }
    }
}
