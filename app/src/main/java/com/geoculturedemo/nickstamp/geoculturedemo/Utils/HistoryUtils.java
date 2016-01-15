package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by nickstamp on 1/15/2016.
 */
public class HistoryUtils {

    private static final String TAG_RECENT_PLACES = "recent_places";
    private static final String TAG_RECENT_SEARCHES = "recent_searches";

    public static void updateRecentPlaces(Context context, String newEntry) {
        updateList(context,newEntry,TAG_RECENT_PLACES);
    }

    public static void updateRecentSearches(Context context, String newEntry) {

        updateList(context,newEntry,TAG_RECENT_SEARCHES);
    }

    public static ArrayList<String> getRecentPlaces(Context context) {
        return  getList(context, TAG_RECENT_PLACES);
    }

    public static ArrayList<String> getRecentSearches(Context context) {
        return  getList(context, TAG_RECENT_SEARCHES);
    }

    private static ArrayList<String> getList(Context context, String tag) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(tag, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);

        if (arrayList == null)
            return new ArrayList<>();

        return arrayList;

    }

    private static void updateList(Context context, String newEntry, String tag) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        ArrayList<String> arrayList = getList(context, tag);

        boolean found = false;
        for (String s : arrayList) {
            if (s.equals(newEntry)) {
                int index = arrayList.indexOf(s);
                arrayList.remove(index);
                arrayList.add(0, newEntry);
                found = true;
                break;
            }
        }

        if (!found) {
            if (arrayList.size() == 3)
                arrayList.remove(2);
            arrayList.add(0, newEntry);
        }

        String json = gson.toJson(arrayList);

        editor.putString(tag, json);
        editor.apply();
    }


}
