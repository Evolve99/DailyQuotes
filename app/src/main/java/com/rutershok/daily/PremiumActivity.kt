package com.rutershok.daily

import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rutershok.daily.adapters.ProAdapter
import com.rutershok.daily.model.ProFeature
import com.rutershok.daily.utils.Ad
import com.rutershok.daily.utils.Premium.getExpirationFormatted
import com.rutershok.daily.utils.Premium.getExpirationSeconds
import com.rutershok.daily.utils.Premium.isPurchased
import com.rutershok.daily.utils.Premium.purchase
import java.util.*

class PremiumActivity : AppCompatActivity() {
    private var mTextPremiumExpiration: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)
        setTitle(R.string.premium)
        if (null != supportActionBar) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val listProFeature = findViewById<ListView>(R.id.list_pro_feature)
        listProFeature.adapter =
            ProAdapter(this, R.layout.item_pro_feature, Arrays.asList(*ProFeature.values()))
        findViewById<View>(R.id.button_rewarded_video).setOnClickListener { v: View? ->
            Ad.showRewardedVideo(
                this@PremiumActivity
            )
        }
        findViewById<View>(R.id.button_survey).setOnClickListener { v: View? -> Ad.showSurvey(this@PremiumActivity) }
        findViewById<View>(R.id.button_buy_premium).setOnClickListener { v: View? -> purchase(this) }
        mTextPremiumExpiration = findViewById(R.id.text_premium_expiration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private var mTimer: CountDownTimer? = null
    private fun startTimer() {
        if (mTimer == null) {
            mTimer = object : CountDownTimer(getExpirationSeconds(this) * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    mTextPremiumExpiration!!.text = getString(
                        R.string.premium_expiration,
                        getExpirationFormatted(this@PremiumActivity)
                    )
                }

                override fun onFinish() {
                    mTextPremiumExpiration!!.text =
                        getString(R.string.premium_expiration, getString(R.string.none))
                }
            }.start()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPurchased) {
            mTextPremiumExpiration!!.text =
                getString(R.string.premium_expiration, getString(R.string.forever))
        } else if (getExpirationSeconds(this) > 0) {
            startTimer()
        } else {
            mTextPremiumExpiration!!.text =
                getString(R.string.premium_expiration, getString(R.string.none))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mTimer != null) {
            mTimer!!.cancel()
        }
    }
}