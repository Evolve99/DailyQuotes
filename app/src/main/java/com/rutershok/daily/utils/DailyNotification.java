package com.rutershok.daily.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.rutershok.daily.MainActivity;
import com.rutershok.daily.R;
import com.rutershok.daily.database.Server;
import com.rutershok.daily.database.Storage;
import com.rutershok.daily.model.DailyQuote;
import com.rutershok.daily.receivers.NotificationsReceiver;

import java.util.Calendar;

public class DailyNotification {

    private static final String CHANNEL_ID = "DAILY_QUOTE_CHANNEL";

    public static void initialize(Context context) {
        if (!Storage.getIsAlarmSetted(context)) {
            Storage.setIsAlarmSetted(context, true);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Storage.getNotificationHour(context));
            calendar.set(Calendar.MINUTE, Storage.getNotificationMinute(context));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constant.RC_DAILY_NOTIFICATION, new Intent(context, NotificationsReceiver.class), 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void send(Context context) {
        Server.getQuoteOfToday(new ParsedRequestListener<DailyQuote>() {
            @Override
            public void onResponse(DailyQuote response) {
                if (response != null && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == Storage.getNotificationHour(context)) {
                    show(context, response.getQuote());
                }
            }

            @Override
            public void onError(ANError anError) {
            }
        });
    }

    public static void show(Context context, String text) {
        // Notification channel ( >= API 26 )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(new NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH));
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.quote_of_the_day))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setContentIntent(PendingIntent.getActivity(context, Constant.RC_DAILY_NOTIFICATION, new Intent(context, MainActivity.class), 0))
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setSmallIcon(R.drawable.ic_quote_white);
            notification.setColor(context.getResources().getColor(android.R.color.transparent));
        } else {
            notification.setSmallIcon(R.mipmap.ic_launcher);
        }

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification.build());
    }
}
