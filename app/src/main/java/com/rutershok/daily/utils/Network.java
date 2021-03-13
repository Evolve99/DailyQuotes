package com.rutershok.daily.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    //Singleton
    private static ConnectivityManager mConnectivityManager;

    private static ConnectivityManager getConnectivityManager(final Context context) {
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return mConnectivityManager;
    }

    public static boolean isWifiConnected(final Context context) {
        try {
            if (getConnectivityManager(context) != null) {
                NetworkInfo networkInfo = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (networkInfo != null) {
                    return networkInfo.isConnected();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    public static boolean isMobileConnected(final Context context) {
        try {
            if (getConnectivityManager(context) != null) {
                NetworkInfo networkInfo = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (networkInfo != null) {
                    return networkInfo.isConnected();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    public static boolean isConnected(final Context context) {
        return isMobileConnected(context) || isWifiConnected(context);
    }
}
