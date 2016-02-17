package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by nickstamp on 1/13/2016.
 */
public class AnimationUtils {

    private static final int ANIM_DURATION_SHORT = 700;

    public static void switchCards(final View cardToShow, final View cardToHide, boolean hasAnimations) {

        if (hasAnimations) {
            YoYo.with(Techniques.ZoomOutRight)
                    .duration(ANIM_DURATION_SHORT)
                    .playOn(cardToHide);
            cardToShow.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.ZoomInLeft)
                    .duration(ANIM_DURATION_SHORT)
                    .playOn(cardToShow);
        } else {
            cardToHide.setVisibility(View.INVISIBLE);
            cardToShow.setVisibility(View.VISIBLE);
        }

    }

    public static void crossfade(View viewToShow, final View viewToHide) {

        viewToShow.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(ANIM_DURATION_SHORT)
                .playOn(viewToShow);

        YoYo.with(Techniques.FadeOut)
                .duration(ANIM_DURATION_SHORT)
                .playOn(viewToHide);

    }


}
