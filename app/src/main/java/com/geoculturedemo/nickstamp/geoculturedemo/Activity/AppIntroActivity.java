package com.geoculturedemo.nickstamp.geoculturedemo.Activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.geoculturedemo.nickstamp.geoculturedemo.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_title_location),
                getString(R.string.intro_text_location),
                R.drawable.ic_maps,
                ContextCompat.getColor(this, R.color.intro_green)));
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_title_movies),
                getString(R.string.intro_text_movies),
                R.drawable.ic_movies,
                ContextCompat.getColor(this, R.color.intro_orange)));
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_title_songs),
                getString(R.string.intro_text_songs),
                R.drawable.ic_headphones,
                ContextCompat.getColor(this, R.color.intro_cyan)));
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_title_local_save),
                getString(R.string.intro_text_local_save),
                R.drawable.ic_save,
                ContextCompat.getColor(this, R.color.intro_blue)));

        setFadeAnimation();

        showStatusBar(false);

        showSkipButton(true);
        setProgressButtonEnabled(true);

    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onBackPressed() {

    }
}
