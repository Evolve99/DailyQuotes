package com.rutershok.daily.utils;

import android.content.Context;
import android.graphics.Color;

import com.rutershok.daily.database.Storage;

public class ColorUtil {

    public static int getBackgroundColor(Context context){
        return Color.parseColor(String.format("#%06X", 0xFFFFFF & Storage.getBackgroundColor(context)));
    }
}
