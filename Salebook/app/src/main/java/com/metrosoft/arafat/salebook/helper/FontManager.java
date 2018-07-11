package com.metrosoft.arafat.salebook.helper;

/**
 * Created by hp on 10/7/2017.
 */
import android.graphics.*;
import android.content.*;

public class FontManager {

    public static final String ROOT = "fonts/",FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}