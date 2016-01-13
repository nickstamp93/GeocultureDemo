package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by nickstamp on 1/13/2016.
 */
public class AnimationUtils {

    private static final int ANIM_DURATION_LONG = 1000;

    public static void switchCards(final View cardToShow, final View cardToHide) {
        
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        cardToShow.setAlpha(0f);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        cardToShow.animate()
                .alpha(1f)
                .setDuration(ANIM_DURATION_LONG)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        cardToShow.setVisibility(View.VISIBLE);
                    }
                });

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        cardToHide.animate()
                .translationX(cardToHide.getWidth())
                .alpha(0.01f)
                .setDuration(ANIM_DURATION_LONG)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cardToHide.setVisibility(View.GONE);
                    }
                });
    }

    public static void crossfade(View viewToShow, final View viewToHide) {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        viewToShow.setAlpha(0f);
        viewToShow.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        viewToShow.animate()
                .alpha(1f)
                .setDuration(ANIM_DURATION_LONG)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        viewToHide.animate()
                .alpha(0f)
                .setDuration(ANIM_DURATION_LONG)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToHide.setVisibility(View.GONE);
                    }
                });
    }


}
