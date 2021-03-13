package com.rutershok.daily.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;

import com.rutershok.daily.R;
import com.rutershok.daily.database.Storage;

import java.util.Locale;

public class Setting {
    @Deprecated
    public static void setLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.locale = locale;
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Deprecated
    public static void setLang(Context context, String language) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        Locale newLocale = new Locale(language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
    }

    public static void contactUs(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + Constant.EMAIL))
                .putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email)));
    }

    public static void openPublisherPage(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Rutershok")));
    }

    public static void launchApp(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            openAppPage(context, packageName);
        }
    }
    public static void openAppPage(Context context, String packageName) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
    }

    public static void openInstagram(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/_u/" + Constant.RUTERSHOK))
                    .setPackage("com.ic_instagram.android"));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + Constant.RUTERSHOK)));
        }
    }

    public static void openFacebook(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.ic_facebook.katana", 0);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + Constant.FACEBOOK_ID)));
        } catch (PackageManager.NameNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + Constant.RUTERSHOK)));
        }
    }

    public static void openTwitter(Context context) {
        Uri uri;
        try {
            context.getPackageManager().getPackageInfo("com.ic_twitter.android", 0);
            uri = Uri.parse("ic_twitter://user?user_id=" + Constant.TWITTER_ID);
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse("https://twitter.com/" + Constant.RUTERSHOK);
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public static void shareApp(Context context) {
        Intent intent = new Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, context.getString(R.string.download_this_app)).setType("text/plain");
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_app)));
    }

    public static void openPrivacyPolicy(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http:s//rutershok.netsons.org/privacy.html")));
    }

    public static boolean saveData(Context context) {
        String saveDataOption = Storage.getSaveData(context);
        if (saveDataOption.equalsIgnoreCase(context.getString(R.string.wifi)) && Network.isWifiConnected(context)) {
            return false;
        } else if (saveDataOption.equalsIgnoreCase(context.getString(R.string.mobile_data)) && Network.isMobileConnected(context)) {
            return false;
        } else if (saveDataOption.equalsIgnoreCase(context.getString(R.string.wifi_mobile_data)) && Network.isConnected(context)) {
            return false;
        } else if (saveDataOption.equalsIgnoreCase(context.getString(R.string.never))) {
            return true;
        }
        return true;
    }

}
