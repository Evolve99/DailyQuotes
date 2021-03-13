package com.rutershok.daily

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.joaquimley.faboptions.FabOptions
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.rutershok.daily.adapters.quotes.WeekQuotesAdapter
import com.rutershok.daily.database.Server
import com.rutershok.daily.model.DailyQuote
import com.rutershok.daily.utils.Share
import com.rutershok.daily.utils.Snackbar
import java.util.*

class WeekActivity : AppCompatActivity() {
    private val mDailyQuotes: MutableList<DailyQuote> = ArrayList()
    private var mViewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week)
        title = getString(R.string.week)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initFabOptions()
        loadWeekQuotes()
    }

    private fun initViewPager() {
        val tabLayout = findViewById<SmartTabLayout>(R.id.tab_week)
        mViewPager = findViewById(R.id.pager_daily)
        (mViewPager as ViewPager).adapter = WeekQuotesAdapter(supportFragmentManager, mDailyQuotes)
        tabLayout.setViewPager(mViewPager)
    }

    private fun loadWeekQuotes() {
        Server.getWeekQuotes(object : ParsedRequestListener<List<DailyQuote>> {
            override fun onResponse(quotes: List<DailyQuote>) {
                if (!quotes.isEmpty()) {
                    mDailyQuotes.addAll(quotes)
                    initViewPager()
                }
            }

            override fun onError(anError: ANError) {
                if (anError.errorDetail == Server.CONNECTION_ERROR) {
                    Snackbar.showNoConnection(this@WeekActivity) { v: View? -> loadWeekQuotes() }
                    //Check server status
                    Server.check(this@WeekActivity)
                }
            }
        })
    }

    private fun initFabOptions() {
        val fabOptions = findViewById<FabOptions>(R.id.fab_week)
        fabOptions.setOnClickListener { v: View ->
            if (fabOptions.isOpen) {
                fabOptions.close(null)
            } else {
                fabOptions.open(null)
            }
            when (v.id) {
                R.id.opt_editor -> mDailyQuotes[mViewPager!!.currentItem].openEditor(this)
                R.id.opt_share -> Share.withText(this, mDailyQuotes[mViewPager!!.currentItem])
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}