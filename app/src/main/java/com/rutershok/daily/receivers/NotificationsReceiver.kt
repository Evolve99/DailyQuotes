package com.rutershok.daily.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rutershok.daily.database.Storage
import com.rutershok.daily.utils.DailyNotification

class NotificationsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Storage.getNotificationsEnabled(context)) {
            DailyNotification.send(context)
        }
    }
}