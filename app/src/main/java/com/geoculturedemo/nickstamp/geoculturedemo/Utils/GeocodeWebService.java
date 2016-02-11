package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnLocaleChanged;
import com.geoculturedemo.nickstamp.geoculturedemo.Callback.OnLocationFound;
import com.geoculturedemo.nickstamp.geoculturedemo.Model.Location;
import com.geoculturedemo.nickstamp.geoculturedemo.R;

import org.json.JSONArray;
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
 * Created by nickstamp on 2/11/2016.
 */
public class GeocodeWebService {


    private static Location getDataFromJSON(String jsonString)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TAG_RESULTS = "results";
        final String TAG_ADDRESS_COMPONENTS = "address_components";
        final String TAG_LONG_NAME = "long_name";
        final String TAG_TYPE = "types";

        Location location = new Location();

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray results = jsonObject.getJSONArray(TAG_RESULTS);

        for (int i = 0; i < results.length(); i++) {

            JSONObject element = results.getJSONObject(i);

            JSONArray types = element.getJSONArray("types");
            JSONArray addressComponents = element.getJSONArray(TAG_ADDRESS_COMPONENTS);

            if (types.toString().contains("locality")) {
                for (int pos = 0; pos < addressComponents.length(); pos++) {
                    JSONObject object = addressComponents.getJSONObject(pos);
                    if (object.getJSONArray(TAG_TYPE).toString().contains("locality")) {
                        location.setArea(object.getString(TAG_LONG_NAME));
                    }
                    if (object.getJSONArray(TAG_TYPE).toString().contains("administrative_area_level_3")) {
                        location.setCity(object.getString(TAG_LONG_NAME));
                    }
                    if (object.getJSONArray(TAG_TYPE).toString().contains("country")) {
                        location.setCountry(object.getString(TAG_LONG_NAME));
                    }
                }
            }

        }

        return location;
    }


    public static void getLocalized(Context context, OnLocaleChanged onLocaleChanged, Location location, Locale locale) {
        ReverseGeocode reverseGeocode = new ReverseGeocode(context, onLocaleChanged, locale);
        reverseGeocode.execute(location.getLatitude(), location.getLongitude());
    }

    public static class ReverseGeocode extends AsyncTask<Double, Void, Location> {

        private OnLocaleChanged onLocaleChanged;
        private Context context;
        private OnLocationFound onLocationFound;
        private boolean isCurrentLocation;
        private Locale locale;

        public ReverseGeocode(Context context, OnLocationFound onLocationFound, boolean isCurrentLocation, Locale locale) {
            this.context = context;
            this.onLocationFound = onLocationFound;
            onLocaleChanged = null;
            this.isCurrentLocation = isCurrentLocation;
            this.locale = locale;
        }

        public ReverseGeocode(Context context, OnLocaleChanged onLocaleChanged, Locale locale) {
            this.context = context;
            this.onLocationFound = null;
            this.onLocaleChanged = onLocaleChanged;
            this.isCurrentLocation = false;
            this.locale = locale;
        }

        @Override
        protected Location doInBackground(Double... params) {
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            } else if (params[0] == 0 && params[1] == 0)
                return null;

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonResponse = null;

            String language = locale.getLanguage();

            try {
                final String BASE_URL =
                        "https://maps.googleapis.com/maps/api/geocode/json?";
                final String PARAM_LATLNG = "latlng";
                final String PARAM_LANG = "language";
                final String PARAM_APIKEY = "key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(PARAM_LATLNG, params[0] + "," + params[1])
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
                Location location = getDataFromJSON(jsonResponse);
                location.setLatitude(params[0]);
                location.setLongitude(params[1]);
                location.setLocale(locale);
                return location;
            } catch (JSONException e) {
                Log.e("nikos", e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the response.
            return null;


        }

        @Override
        protected void onPostExecute(Location location) {
            if (onLocaleChanged == null)
                onLocationFound.onLocationFound(location, isCurrentLocation);
            else if (onLocationFound == null)
                onLocaleChanged.onLocaleChanged(location);

        }
    }

}
