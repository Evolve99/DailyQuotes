package com.rutershok.daily.database;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.rutershok.daily.model.DailyQuote;
import com.rutershok.daily.model.Quote;
import com.rutershok.daily.utils.Network;
import com.rutershok.daily.utils.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Server {

    //Errors
    public static final String CONNECTION_ERROR = "connectionError";

    private static final String API = "https://www.daily.netsons.org/api/";
    private static final String GET_QUOTES = "get_quotes.php";
    private static final String SEARCH_QUOTES = "search_quotes.php";
    private static final String GET_DAILY_QUOTES = "get_daily_quotes.php";
    private static final String GET_DAILY_QUOTE = "get_daily_quote.php";
    private static final String GET_WEEK_QUOTES = "get_week_quotes.php";
    private static final String GET_IMAGES = "get_images.php";
    private static final String CATEGORY = "category";
    private static final String QUERY = "query";
    private static final String FROM = "from";
    private static final String INTERVAL = "interval";
    private static final String LANGUAGE = "language";
    private static final String GET_RANDOM_QUOTE = "get_random_quotes.php";
    private static final String IMAGES_DIRECTORY = "https://www-daily.netsons.org/images/";

    private static final int STATUS_OK = 200;

    private static ANRequest.GetRequestBuilder getAPI(String url) {
        return AndroidNetworking.get(API + url).addQueryParameter(LANGUAGE, Locale.getDefault().getLanguage());
    }

    public static void initialize(Activity activity) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(activity, okHttpClient);
        AndroidNetworking.enableLogging();
    }

    public static void getDailyQuotes(int from, ParsedRequestListener<List<DailyQuote>> listener) {
        getAPI(GET_DAILY_QUOTES)
                .addQueryParameter(FROM, String.valueOf(from))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObjectList(DailyQuote.class, listener);
    }

    public static void getWeekQuotes(ParsedRequestListener<List<DailyQuote>> listener) {
        getAPI(GET_WEEK_QUOTES)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObjectList(DailyQuote.class, listener);
    }

    public static void getQuotes(Context context, String category, int from, ParsedRequestListener<List<Quote>> listener) {
        getAPI(GET_QUOTES)
                .addQueryParameter(FROM, String.valueOf(from))
                .addQueryParameter(CATEGORY, category)
                .addQueryParameter(INTERVAL, Storage.getIntervalName(context))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObjectList(Quote.class, listener);
    }

    public static void searchQuotes(String query, ParsedRequestListener<List<Quote>> listener) {
        getAPI(SEARCH_QUOTES)
                .addQueryParameter(QUERY, query)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObjectList(Quote.class, listener);
    }

    //Load daily quote for notification
    public static void getQuoteOfToday(ParsedRequestListener<DailyQuote> listener) {
        getAPI(GET_DAILY_QUOTE)
                .addQueryParameter(LANGUAGE, Locale.getDefault().getLanguage())
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(DailyQuote.class, listener);
    }

    public static void getRandomQuotes(ParsedRequestListener<List<Quote>> listener) {
        getAPI(GET_RANDOM_QUOTE)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObjectList(Quote.class, listener);

    }

    public static void check(Activity activity) {
        if (Network.isConnected(activity)) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... v) {
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(API).openConnection();
                        httpURLConnection.setConnectTimeout(5 * 1000); //5 sec
                        httpURLConnection.connect();
                        return STATUS_OK == httpURLConnection.getResponseCode();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean isOnline) {
                    super.onPostExecute(isOnline);
                    if (!isOnline) {
                        Snackbar.showLowInternet(activity);
                    }
                }
            }.execute();
        }
    }

    @Deprecated
    public static void cacheAllImages(Context context) {
        new AsyncTask<Void, Void, Void>() {

            final List<String> imagesName = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... v) {
                ANRequest request = AndroidNetworking.get(API + GET_IMAGES)
                        .setPriority(Priority.HIGH)
                        .build();

                ANResponse response = request.executeForString();

                if (response.isSuccess()) {
                    imagesName.addAll(Arrays.asList(response.getResult().toString().split("<br />")));
                } else {
                    ANError error = response.getError();
                    Log.e("Error", error.getErrorBody() + " " + error.getErrorDetail() + " " + error.getErrorCode());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                if (!imagesName.isEmpty()) {
                    for (String s : imagesName) {

                        Glide.with(context).downloadOnly().load(IMAGES_DIRECTORY + s).submit();
                        Log.e("Downloading", s);
                    }
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}