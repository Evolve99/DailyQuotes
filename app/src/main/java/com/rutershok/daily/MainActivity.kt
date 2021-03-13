package com.rutershok.daily

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.rutershok.daily.adapters.quotes.DailyQuotesAdapter
import com.rutershok.daily.database.Server
import com.rutershok.daily.model.DailyQuote
import com.rutershok.daily.utils.DailyNotification
import com.rutershok.daily.utils.Dialog
import com.rutershok.daily.utils.Premium.initialize
import com.rutershok.daily.utils.Snackbar.show
import com.rutershok.daily.utils.Snackbar.showNoConnection
import com.rutershok.daily.view.DrawerLayout
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mDailyQuotes: MutableList<DailyQuote> = ArrayList()
    private var mDailyQuotesAdapter: DailyQuotesAdapter? = null
    private var mSwipeRefresh: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize(this)
        Server.initialize(this)
        Server.check(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle(R.string.daily_quotes)
        DrawerLayout(this, toolbar)
        initSwipeRefresh()
        initRecyclerView()
        loadDailyQuotes()
        DailyNotification.initialize(this)
        Dialog.showRateThisApp(this)
    }

    private fun initSwipeRefresh() {
        // SwipeRefreshLayout
        mSwipeRefresh = findViewById(R.id.swipe_daily_quotes)

        //Load quotes is automatically called when recyclerView is cleared
        //getDailyQuotes();
        (mSwipeRefresh as SwipeRefreshLayout).setOnRefreshListener {
            mDailyQuotes.clear()
            mDailyQuotesAdapter!!.notifyDataSetChanged()
        }
        (mSwipeRefresh as SwipeRefreshLayout).setColorSchemeResources(
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
    }

    private fun initRecyclerView() {
        mDailyQuotesAdapter = DailyQuotesAdapter(this, mDailyQuotes)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_daily_quotes)
        recyclerView.adapter = mDailyQuotesAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadDailyQuotes()
                    // Ad.showInterstitialFrequency(MainActivity.this);
                }
            }
        })
    }

    private fun loadDailyQuotes() {
        mSwipeRefresh!!.isRefreshing = true
        Server.getDailyQuotes(mDailyQuotes.size, object : ParsedRequestListener<List<DailyQuote>> {
            override fun onResponse(response: List<DailyQuote>) {
                mSwipeRefresh!!.isRefreshing = false
                mDailyQuotes.addAll(response)
                mDailyQuotesAdapter!!.notifyDataSetChanged()
                if (response.isEmpty()) {
                    show(this@MainActivity, R.string.quotes_finished)
                }
            }

            override fun onError(anError: ANError) {
                mSwipeRefresh!!.isRefreshing = false
                if (anError.errorDetail == Server.CONNECTION_ERROR) {
                    showNoConnection(this@MainActivity) { v: View? -> loadDailyQuotes() }
                    //Check server status
                    Server.check(this@MainActivity)
                }
            }
        })
    }

    override fun onBackPressed() {
        Dialog.showExit(this)
    }

    override fun onResume() {
        super.onResume()
        //        Ad.showBanner(this);
    }
}