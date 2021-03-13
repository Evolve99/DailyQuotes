package com.rutershok.daily

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.joaquimley.faboptions.FabOptions
import com.rutershok.daily.adapters.quotes.RandomQuotesAdapter
import com.rutershok.daily.database.Server
import com.rutershok.daily.database.Storage
import com.rutershok.daily.model.Quote
import com.rutershok.daily.utils.Ad
import com.rutershok.daily.utils.ImageUtil
import com.rutershok.daily.utils.Share
import com.rutershok.daily.utils.Snackbar.show
import com.rutershok.daily.utils.Snackbar.showNoConnection
import com.yuyakaido.android.cardstackview.*
import java.util.*

class RandomActivity : AppCompatActivity() {
    private val mQuotes: MutableList<Quote> = ArrayList()
    private var mAdapter: RandomQuotesAdapter? = null
    private var mCardStackView: CardStackView? = null
    private var mFabOptions: FabOptions? = null
    private var mView: View? = null
    private var mCurrentPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        setTitle(R.string.random)
        if (null != supportActionBar) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        initCardStack()
        initFabOptions()
        loadRandomQuotes()
        Ad.showBanner(this)
    }

    private fun initCardStack() {
        mAdapter = RandomQuotesAdapter(this, mQuotes)
        mCardStackView = findViewById(R.id.card_stack_view)
        (mCardStackView as CardStackView).adapter = mAdapter
        val manager = CardStackLayoutManager(this@RandomActivity, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {}
            override fun onCardSwiped(direction: Direction) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View, position: Int) {
                mView = view
                mCurrentPosition = position
                updateButtonColor()
            }

            override fun onCardDisappeared(view: View, position: Int) {
                if (mQuotes.size - position == 3) {
                    loadRandomQuotes()
                }
            }
        })
        manager.setStackFrom(StackFrom.Top)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        mCardStackView!!.layoutManager = manager
    }

    private fun loadRandomQuotes() {
        Server.getRandomQuotes(object : ParsedRequestListener<List<Quote>> {
            override fun onResponse(quotes: List<Quote>) {
                if (!quotes.isEmpty()) {
                    mAdapter!!.notifyItemRangeInserted(mQuotes.size, quotes.size)
                    mQuotes.addAll(quotes)
                }
            }

            override fun onError(anError: ANError) {
                if (anError.errorDetail == Server.CONNECTION_ERROR) {
                    showNoConnection(this@RandomActivity) { v: View? -> loadRandomQuotes() }

                    //Check server status
                    Server.check(this@RandomActivity)
                }
            }
        })
    }

    private fun initFabOptions() {
        mFabOptions = findViewById(R.id.fab_random)
        (mFabOptions as FabOptions).setOnClickListener { v: View ->
            if (mFabOptions!!.isOpen) {
                mFabOptions!!.close(null)
            } else {
                mFabOptions!!.open(null)
            }
            val quote = mQuotes[mCurrentPosition]
            when (v.id) {
                R.id.opt_favorite -> {
                    if (quote.isFavorite(this)) {
                        show(this, R.string.removed_from_favorites)
                        Storage.removeFavoriteQuote(this, quote)
                    } else {
                        show(this, R.string.added_to_favorites)
                        Storage.addFavoriteQuote(this, quote)
                    }
                    updateButtonColor()
                }
                R.id.opt_editor -> quote.openEditor(this)
                R.id.opt_save -> ImageUtil.saveImage(
                    this,
                    ImageUtil.getScaledBitmapWatermark(
                        this,
                        mView!!.findViewById(R.id.constraint_random_quote)
                    )
                )
                R.id.opt_share -> Share.withText(this, quote)
            }
        }
    }

    private fun updateButtonColor() {
        if (mQuotes[mCurrentPosition].isFavorite(this@RandomActivity)) {
            mFabOptions!!.setButtonColor(R.id.opt_favorite, R.color.yellow_700)
        } else {
            mFabOptions!!.setButtonColor(R.id.opt_favorite, R.color.white)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_rewind -> mCardStackView!!.rewind()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_random, menu)
        return super.onCreateOptionsMenu(menu)
    }
}