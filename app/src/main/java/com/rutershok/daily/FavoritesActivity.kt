package com.rutershok.daily

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.daily.adapters.quotes.QuotesAdapter
import com.rutershok.daily.database.Storage
import com.rutershok.daily.utils.Ad

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setTitle(R.string.favorites)
        if (null != supportActionBar) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        initRecyclerView()
        Ad.showBanner(this)
        //        Ad.showInterstitialFrequency(this);
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_favorites)
        recyclerView.setHasFixedSize(true)

        //Load favorite logo_phrases
        if (Storage.getFavoriteQuotes(this).isEmpty()) {
            //Show text "No favorite logo_phrases"
            findViewById<View>(R.id.text_no_favorite_quotes).visibility = View.VISIBLE
        } else {
            recyclerView.adapter = QuotesAdapter(this, Storage.getFavoriteQuotes(this))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}