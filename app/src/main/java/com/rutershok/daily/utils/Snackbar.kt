package com.rutershok.daily.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.rutershok.daily.R
import de.mateware.snacky.Snacky

object Snackbar {

    @JvmStatic
    fun show(activity: Activity, textRes: Int) {
        Snacky.builder()
            .setActivity(activity)
            .setTextSize(16f)
            .setTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_SHORT)
            .setText(activity.getString(textRes))
            .setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
            .build()
            .show()
    }

    @JvmStatic
    fun showError(activity: Activity, textRes: Int) {
        Snacky.builder()
            .setActivity(activity)
            .setTextSize(16f)
            .setTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_SHORT)
            .setText(activity.getString(textRes))
            .error()
            .show()
    }

    @JvmStatic
    fun showNoConnection(activity: Activity?, clickListener: View.OnClickListener?) {
        Snacky.builder().setActivity(activity)
            .setTextSize(16f)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_INDEFINITE)
            .setText(R.string.no_internet_connection)
            .setActionText(R.string.reload)
            .setActionClickListener(clickListener)
            .setIcon(R.drawable.ic_wifi_off_white)
            .error()
            .show()
    }

    @JvmStatic
    fun showLowInternet(activity: Activity?) {
        Snacky.builder()
            .setActivity(activity)
            .setTextSize(16f)
            .setTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_LONG)
            .setText(R.string.low_connection)
            .setActionClickListener(null)
            .setIcon(R.drawable.ic_wifi_off_white)
            .setActionTextColor(ContextCompat.getColor(activity!!, R.color.white))
            .setActionText(android.R.string.ok)
            .error()
            .show()
    }
}