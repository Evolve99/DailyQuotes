package com.rutershok.daily

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.rutershok.daily.adapters.quotes.QuotesAdapter
import com.rutershok.daily.database.Server
import com.rutershok.daily.model.Quote
import com.rutershok.daily.utils.*
import com.rutershok.daily.utils.Snackbar.show
import com.rutershok.daily.utils.Snackbar.showNoConnection
import java.util.*

class QuotesActivity : AppCompatActivity() {
    private val mQuotes: MutableList<Quote> = ArrayList()
    private var mQuotesAdapter: QuotesAdapter? = null
    private var mSwipeRefresh: SwipeRefreshLayout? = null
    private var mCategory: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
        title = intent.getStringExtra(Constant.CATEGORY_TITLE)
        mCategory = intent.getStringExtra(Constant.CATEGORY_KEY)
        if (null != supportActionBar) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        initSwipeRefresh()
        initRecyclerView()

        //Load logo_phrases for first time, after onScroll
        loadQuotes()
        Ad.showBanner(this)
        // Ad.showInterstitialFrequency(this);
    }

    private fun loadQuotes() {
        mSwipeRefresh!!.isRefreshing = true
        Server.getQuotes(
            this,
            mCategory,
            mQuotes.size,
            object : ParsedRequestListener<List<Quote>?> {
                override fun onResponse(response: List<Quote>?) {
                    mSwipeRefresh!!.isRefreshing = false
                    if (null != response) {
                        mQuotes.addAll(response)
                        mQuotesAdapter!!.notifyDataSetChanged()

                        //Quotes are finished
                        if (response.isEmpty()) {
                            show(this@QuotesActivity, R.string.quotes_finished)
                        }
                    }
                }

                override fun onError(anError: ANError) {
                    mSwipeRefresh!!.isRefreshing = false
                    if (anError.errorDetail == Server.CONNECTION_ERROR) {
                        showNoConnection(this@QuotesActivity) { v: View? -> loadQuotes() }

                        //Check server status
                        Server.check(this@QuotesActivity)
                    }
                }
            })
    }

    private fun initSwipeRefresh() {
        // SwipeRefreshLayout
        mSwipeRefresh = findViewById(R.id.swipe_quotes)
        //getQuotes(); called inside recycler listener
        (mSwipeRefresh as SwipeRefreshLayout).setOnRefreshListener {
            mQuotes.clear()
            mQuotesAdapter!!.notifyDataSetChanged()
        }
        (mSwipeRefresh as SwipeRefreshLayout).setColorSchemeResources(
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
    }

    private fun initRecyclerView() {
        mQuotesAdapter = QuotesAdapter(this, mQuotes)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_quotes)
        recyclerView.adapter = mQuotesAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadQuotes()
                    //Ad.showInterstitialFrequency(QuotesActivity.this);
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_quote, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_interval ->                 //Show this dialog to choose the refresh interval of logo_phrases
                Dialog.showSelectInterval(this)
        }
        return super.onOptionsItemSelected(item)
    }
}