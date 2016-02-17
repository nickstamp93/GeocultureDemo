package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.geoculturedemo.nickstamp.geoculturedemo.R;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_animations)));

        findPreference(getString(R.string.pref_key_about)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.alert_dialog_style)
                        .setTitle(getString(R.string.pref_label_about))
                        .setMessage(getString(R.string.text_dialog_version_info))
                        .setPositiveButton(getString(android.R.string.ok), null)
                        .setNegativeButton(getString(android.R.string.cancel), null);
                builder.show();
                return false;
            }
        });
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), true));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();


        if (preference instanceof CheckBoxPreference) {
            if (stringValue.equals("true"))
                stringValue = getString(R.string.text_enabled);
            else
                stringValue = getString(R.string.text_disabled);
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }


        return true;
    }

}