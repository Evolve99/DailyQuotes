package com.rutershok.daily.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FontUtil {

    public static List<String> getFontsName(Context context) {
        List<String> fonts = new ArrayList<>();

        try {
            String[] namesList = new String[0];
            AssetManager assetManager = context.getAssets();
            if (assetManager != null) {
                namesList = assetManager.list("fonts");
            }
            if (namesList != null) {
                fonts.addAll(Arrays.asList(namesList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fonts;
    }

    public static Typeface getFont(Context context, String fontName) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
    }

    public static boolean getIsPremium(String font) {
        return font.contains("premium");
    }
}
