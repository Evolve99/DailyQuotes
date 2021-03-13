package com.rutershok.daily.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rutershok.daily.R;
import com.rutershok.daily.model.Interval;
import com.rutershok.daily.model.Quote;
import com.rutershok.daily.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Storage {

    //Shared preferences singleton
    private static SharedPreferences preferences;

    private static SharedPreferences getPreferences(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return preferences;
    }

    //Gson singleton
    private static Gson gson;

    private static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static void addFavoriteQuote(Context context, Quote quote) {
        List<Quote> favoriteQuotes = getFavoriteQuotes(context);
        if (!favoriteQuotes.contains(quote)) {
            favoriteQuotes.add(quote);

            //Interval shared preferences
            setFavoriteQuotes(context, favoriteQuotes);
        }
    }

    public static void removeFavoriteQuote(Context context, Quote quote) {
        List<Quote> favoriteQuotes = getFavoriteQuotes(context);
        favoriteQuotes.remove(quote);

        //Interval shared preferences
        setFavoriteQuotes(context, favoriteQuotes);
    }

    private static void setFavoriteQuotes(Context context, List<Quote> favoriteQuotes) {
        getPreferences(context).edit().putString(Constant.FAVORITE_QUOTES, getGson().toJson(favoriteQuotes)).apply();
    }

    public static List<Quote> getFavoriteQuotes(Context context) {
        List<Quote> favoriteQuotes = getGson().fromJson(getPreferences(context).getString(Constant.FAVORITE_QUOTES, null), //Get string
                new TypeToken<List<Quote>>() {
                }.getType());
        return null == favoriteQuotes ? new ArrayList<>() : favoriteQuotes;
    }

    public static int getIntervalId(Context context) {
        return getPreferences(context).getInt(Constant.INTERVAL_ID, Interval.DAY.ordinal());
    }

    public static void setIntervalId(Context context, int id) {
        getPreferences(context).edit().putInt(Constant.INTERVAL_ID, id).apply();
    }

    public static String getIntervalName(Context context) {
        return getPreferences(context).getString(Constant.INTERVAL_NAME, context.getString(Interval.DAY.getNameRes()));
    }

    public static void setIntervalName(Context context, String name) {
        getPreferences(context).edit().putString(Constant.INTERVAL_NAME, name).apply();
    }

    public static boolean getNotificationsEnabled(Context context) {
        return getPreferences(context).getBoolean(Constant.PREF_NOTIFICATIONS, true);
    }

    public static void setNotificationsEnabled(Context context, boolean isEnabled) {
        getPreferences(context).edit().putBoolean(Constant.PREF_NOTIFICATIONS, isEnabled).apply();
    }

    public static void setNotificationHour(Context context, int hour) {
        getPreferences(context).edit().putInt(Constant.NOTIFICATION_HOUR, hour).apply();
    }

    public static int getNotificationHour(Context context) {
        return getPreferences(context).getInt(Constant.NOTIFICATION_HOUR, 8);
    }

    public static void setNotificationMinute(Context context, int minute) {
        getPreferences(context).edit().putInt(Constant.NOTIFICATION_MINUTE, minute).apply();
    }

    public static int getNotificationMinute(Context context) {
        return getPreferences(context).getInt(Constant.NOTIFICATION_MINUTE, 30);
    }

    @Deprecated
    public static void setLanguage(Context context, String language) {
        getPreferences(context).edit().putString(Constant.LANGUAGE, language).apply();
    }

    @Deprecated
    public static String getLanguage(Context context) {
        return getPreferences(context).getString(Constant.LANGUAGE, Locale.getDefault().getLanguage());
    }

    public static void setAnimations(Context context, boolean isEnabled) {
        getPreferences(context).edit().putBoolean(Constant.ANIMATION, isEnabled).apply();
    }

    private static void setInterstitialCount(Context context, int count) {
        getPreferences(context).edit().putInt(Constant.INTERSTITIAL_COUNT, count).apply();
    }

    public static int getInterstitialCount(Context context) {
        return getPreferences(context).getInt(Constant.INTERSTITIAL_COUNT, 1);
    }

    public static void updateInterstitialCount(Context context) {
        setInterstitialCount(context, getInterstitialCount(context) + 1);
        Log.e("Updated", getInterstitialCount(context)+"");
    }

    public static void setBackgroundColor(Context context, int color) {
        getPreferences(context).edit().putInt(Constant.PREF_BACKGROUND_COLOR, color).apply();
    }

    public static int getBackgroundColor(Context context) {
        return getPreferences(context).getInt(Constant.PREF_BACKGROUND_COLOR, -16777216);
    }

    public static void setTextColor(Context context, int color) {
        getPreferences(context).edit().putInt(Constant.PREF_TEXT_COLOR, color).apply();
    }

    public static int getTextColor(Context context) {
        return getPreferences(context).getInt(Constant.PREF_TEXT_COLOR, -1);
    }

    public static void setSaveData(Context context, String value) {
        Log.e("New Value", value);
        getPreferences(context).edit().putString(Constant.PREF_DATA_SAVING, value).apply();
    }

    public static String getSaveData(Context context) {
        return getPreferences(context).getString(Constant.PREF_DATA_SAVING, context.getString(R.string.wifi_mobile_data));
    }

    private static void setPremiumExpirationMillis(Context context, long value) {
        getPreferences(context).edit().putLong(Constant.EXPIRATION_TIME, value).apply();
    }

    public static long getPremiumExpirationMillis(Context context) {
        return getPreferences(context).getLong(Constant.EXPIRATION_TIME, System.currentTimeMillis());
    }

    public static void updatePremiumExpirationMillis(Context context, long value) {
        //Check if expired, reset to current time millis
        if (getPremiumExpirationMillis(context) - System.currentTimeMillis() < 0) {
            setPremiumExpirationMillis(context, System.currentTimeMillis() + value);
        } else {
            setPremiumExpirationMillis(context, getPremiumExpirationMillis(context) + value);
        }
    }

    public static boolean getIsAlarmSetted(Context context){
        return getPreferences(context).getBoolean(Constant.ALARM_NOTIFICATION,false);
    }

    public static void setIsAlarmSetted(Context context, boolean b){
        getPreferences(context).edit().putBoolean(Constant.ALARM_NOTIFICATION, b).apply();
    }
}
