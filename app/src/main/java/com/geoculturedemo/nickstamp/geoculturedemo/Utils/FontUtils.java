package com.geoculturedemo.nickstamp.geoculturedemo.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nickstamp on 1/14/2016.
 */
public class FontUtils {

    public interface FontTypes {
        String LIGHT = "Light";
        String BOLD = "Bold";
        String REGULAR = "Regular";
    }

    private static Map fontMap = new HashMap();

    static {
        fontMap.put(FontTypes.LIGHT, "fonts/Roboto-Light.ttf");
        fontMap.put(FontTypes.BOLD, "fonts/Roboto-Bold.ttf");
        fontMap.put(FontTypes.REGULAR, "fonts/Roboto-Regular.ttf");
    }

    private static Map typefaceCache = new HashMap();

    /**
     * Creates Roboto typeface and puts it into cache
     *
     * @param context
     * @param fontType
     * @return
     */
    private static Typeface getRobotoTypeface(Context context, String fontType) {
        String fontPath = ((String) fontMap.get(fontType));
        if (!typefaceCache.containsKey(fontType)) {
            typefaceCache.put(fontType, Typeface.createFromAsset(context.getAssets(), fontPath));
        }
        return ((Typeface) typefaceCache.get(fontType));
    }

    /**
     * Gets roboto typeface according to passed typeface style settings.
     * Will get Roboto-Bold for Typeface.BOLD etc
     *
     * @param context
     * @return
     */
    private static Typeface getRobotoTypeface(Context context, Typeface originalTypeface) {
        String robotoFontType = FontTypes.REGULAR; //default Regular Roboto font
        if (originalTypeface != null) {
            int style = originalTypeface.getStyle();
            switch (style) {
                case Typeface.BOLD:
                    robotoFontType = FontTypes.BOLD;
                    break;
                case Typeface.ITALIC:
                    robotoFontType = FontTypes.LIGHT;
                    break;
            }
        }
        return getRobotoTypeface(context, robotoFontType);
    }

    /**
     * Walks ViewGroups, finds TextViews and applies Typefaces taking styling in consideration
     *
     * @param context - to reach assets
     * @param view    - root view to apply typeface to
     */
    public static void setRobotoFont(Context context, View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setRobotoFont(context, ((ViewGroup) view).getChildAt(i));
            }
        } else if (view instanceof TextView) {
            Typeface currentTypeface = ((TextView) view).getTypeface();
            ((TextView) view).setTypeface(getRobotoTypeface(context, currentTypeface));
        }
    }
}