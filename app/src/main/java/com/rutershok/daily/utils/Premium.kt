package com.rutershok.daily.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.rutershok.daily.R
import com.rutershok.daily.database.Storage
import com.rutershok.daily.utils.Snackbar.show
import java.util.*
import java.util.concurrent.TimeUnit

object Premium {
    private var mBillingProcessor: BillingProcessor? = null
    @JvmStatic
    fun initialize(activity: Activity) {
        mBillingProcessor = BillingProcessor(
            activity,
            Constant.GOOGLE_PLAY_KEY,
            Constant.MERCHANT_ID,
            object : BillingProcessor.IBillingHandler {
                override fun onProductPurchased(productId: String, details: TransactionDetails?) {}
                override fun onPurchaseHistoryRestored() {}
                override fun onBillingError(errorCode: Int, error: Throwable?) {
                    Toast.makeText(
                        activity,
                        "Billing error, restart the app or check your connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onBillingInitialized() {
                    if (!isPremium(activity) && !isPurchased) {
                        Ad.initialize(activity) //MainActivity
                    }
                }
            })
    }
    @JvmStatic
    val isBillingInitialized: Boolean
        get() = mBillingProcessor != null && mBillingProcessor!!.isInitialized

    @JvmStatic
    fun purchase(activity: Activity?) {
        if (!isPurchased) {
            if (isBillingInitialized) {
                mBillingProcessor!!.purchase(activity, Constant.PREMIUM_VERSION)
            }
        } else {
            show(activity!!, R.string.you_already_have_premium_version)
        }
    }

    @JvmStatic
    val isPurchased: Boolean
        get() = isBillingInitialized && mBillingProcessor!!.isPurchased(Constant.PREMIUM_VERSION)

    @JvmStatic
    fun isPremium(context: Context): Boolean {
        return isPurchased || 0 < Storage.getPremiumExpirationMillis(context) - System.currentTimeMillis()
    }

    @JvmStatic
    fun getExpirationSeconds(context: Context): Long {
        return (Storage.getPremiumExpirationMillis(context) - System.currentTimeMillis()) / 1000
    }

    @JvmStatic
    fun getExpirationFormatted(context: Context): String {
        val seconds = getExpirationSeconds(context)
        return String.Companion.format(
            Locale.getDefault(), "%dd %dh %dm %ds",
            TimeUnit.SECONDS.toDays(seconds),
            TimeUnit.SECONDS.toHours(seconds) % TimeUnit.DAYS.toHours(1),
            TimeUnit.SECONDS.toMinutes(seconds) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.SECONDS.toSeconds(seconds) % TimeUnit.MINUTES.toSeconds(1)
        )
    }
}